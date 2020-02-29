package com.nidotim.lottery.service;

import com.nidotim.lottery.model.Game;
import com.nidotim.lottery.model.Lottery;
import com.nidotim.lottery.model.Ticket;
import com.nidotim.lottery.model.User;
import com.nidotim.lottery.model.enums.GameStatus;
import com.nidotim.lottery.repository.GameRepository;
import com.nidotim.lottery.repository.TicketRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

  private final GameRepository gameRepository;
  private final TicketRepository ticketRepository;
  private final GameScanService gameScanService;

  public Game createGame(Game game) {
    return gameRepository.save(game);
  }

  public List<Game> findAllUsers() {
    return gameRepository.findAll();
  }

  public Game getOne(String id) {
    return gameRepository.findById(id).get();
  }

  public Game getCurrentGame(Lottery lottery) {
    return gameRepository.findByLotteryIsAndStatusIs(lottery, GameStatus.Opening);
  }

  public Game startGame(Lottery lottery) {
    if(gameRepository.existsByLotteryIsAndStatusIs(lottery, GameStatus.Opening)) {
      throw new IllegalStateException();
    }
    Instant startDate = Instant.now();
    Instant endDate = Instant.now().plusSeconds(60 * 30);
    Game game = Game.builder()
        .lottery(lottery)
        .startDate(startDate)
        .endDate(endDate)
        .status(GameStatus.Opening)
        .build();

    return createGame(game);
  }

  public Page<Game> findExpired(Pageable pageable) {
    Instant now = Instant.now();
    return gameRepository.findByStatusIsAndEndDateBefore(GameStatus.Opening, now, pageable);
  }

  @Transactional
  public void end(Game game) {
    game.setStatus(GameStatus.Ended);
    gameRepository.save(game);
  }

  public Page<Game> findByStatus(GameStatus status, Pageable pageable) {
    return gameRepository.findByStatusIs(status, pageable);
  }

  @Transactional
  public Game open(Game game) {
    game.open();
    game =  gameRepository.save(game);
    startGame(game.getLottery());
    return game;
  }

  public void scan(String id) {
    PageRequest pageable = PageRequest.of(0, 500);
    //Game game = gameRepository.findById(id).get();
    gameScanService.startScanGame(id);
    //game = gameRepository.findById(game.getId()).get();
    Set<Integer> winningNumbers = gameScanService.getWinningNumbers(id);
    int leastWinningNumber = gameScanService.getLeastWinningNumber(id);
    Page<Ticket> ticketPage = gameScanService.findUnscannedTickets(id, pageable);
    while (ticketPage.getTotalElements() > 0) {
      log.debug("scan ... " + ticketPage.getTotalElements());
      gameScanService.scanTickets(ticketPage.getContent(), winningNumbers, leastWinningNumber);
      ticketPage = gameScanService.findUnscannedTickets(id, pageable);
    }

    //game = gameRepository.findById(game.getId()).get();
    gameScanService.endScanGame(id);
    //game.setStatus(GameStatus.Closed);
    //return gameRepository.save(game);
  }

  public void buyTicket(User user, String gameId, List<Integer> numberList) {
    Optional<Game> gameOpt = gameRepository.findById(gameId);
    if(gameOpt.isPresent()) {
      Game game = gameOpt.get();
      if(game.isEnded()) {
        return;
      }
      Ticket ticket = game.buyTicket(user, numberList);
      ticketRepository.save(ticket);
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void buyTicket(User user, String gameId, Integer numOfTickets) {
    Optional<Game> gameOpt = gameRepository.findById(gameId);
    if(gameOpt.isPresent()) {
      Game game = gameOpt.get();
      if(game.isEnded()) {
        return;
      }
      List<Ticket> tickets = game.buyTickets(user, numOfTickets);
      ticketRepository.saveAll(tickets);
    }
  }

}
