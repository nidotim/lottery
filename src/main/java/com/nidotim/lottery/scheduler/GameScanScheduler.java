package com.nidotim.lottery.scheduler;

import com.nidotim.lottery.model.Game;
import com.nidotim.lottery.model.enums.GameStatus;
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
public class GameScanScheduler extends BaseCleanUpScheduler<Game> {


  @Autowired
  private GameService gameService;

  public GameScanScheduler(
      @Value("${application.service.scan-game.cron.enabled}") boolean enabled,
      @Value("${application.service.scan-game.expiry-time}") int expiryTime) {
    super(enabled, expiryTime, 100, false);
  }

  /**
   * Clean up expired questions
   */
  @Scheduled(cron = "${application.service.scan-game.cron.schedule}")
  @Transactional
  @SchedulerLock(name = "GameScanScheduler", lockAtLeastForString = "PT10S", lockAtMostForString = "PT300S")
  @Override
  public void schedulerFired() {
    log.info("Scan Game Scheduler fired.  enabled: {}, expiryTime: {} seconds ", enabled,
        expiryTime);
    super.schedulerFired();
  }

  @Override
  Page<Game> findExpired(Instant expiredDate, Pageable pageable) {
    return gameService.findByStatus(GameStatus.Opened, pageable);
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
      gameService.scan(question);
    } catch (Exception e) {
      log.error("scan game failed. id: {}. {}", question.getId(), e.getMessage());
      //ignore
    }
  }

  @Override
  String getErrorMessage(Game game) {
    return "scan game failed.  id: " + game.getId();
  }

}