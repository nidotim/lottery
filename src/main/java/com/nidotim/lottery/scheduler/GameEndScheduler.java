package com.nidotim.lottery.scheduler;

import com.nidotim.lottery.model.Game;
import com.nidotim.lottery.service.GameService;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class GameEndScheduler extends BaseCleanUpScheduler<Game> {


  @Autowired
  private GameService gameService;

  public GameEndScheduler(
      @Value("${application.service.end-game.cron.enabled}") boolean enabled,
      @Value("${application.service.end-game.expiry-time}") int expiryTime) {
    super(enabled, expiryTime, 100, false);
  }

  /**
   * Clean up expired questions
   */
  @Scheduled(cron = "${application.service.end-game.cron.schedule}")
  @Transactional
  @SchedulerLock(name = "GameEndScheduler", lockAtLeastForString = "PT10S")
  @Override
  public void schedulerFired() {
    log.info("End Game Scheduler fired.  enabled: {}, expiryTime: {} seconds ", enabled,
        expiryTime);
    super.schedulerFired();
  }

  @Override
  Page<Game> findExpired(Instant expiredDate, Pageable pageable) {
    Page<Game> gamePage = gameService.findExpired(pageable);
    log.info("End Game Found: " + gamePage.getTotalElements());
    return gamePage;
  }

  @Override
  void batchCleanUp(Page<Game> entityPage) {
    for (Game game : entityPage) {
      cleanUp(game);
    }
  }

  @Override
  String getBatchErrorMessage(Page<Game> entityPage) {
    return "batch ended game failed.  size: " + entityPage.getSize();
  }

  @Override
  void cleanUp(Game question) {
    try {
      gameService.end(question);
    } catch (Exception e) {
      log.error("ended game failed. id: {}. {}", question.getId(), e.getMessage());
      //ignore
    }
  }

  @Override
  String getErrorMessage(Game game) {
    return "ended game failed.  id: " + game.getId();
  }

}