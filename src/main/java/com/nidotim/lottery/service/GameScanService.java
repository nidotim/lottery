package com.nidotim.lottery.service;

import com.nidotim.lottery.model.Game;
import com.nidotim.lottery.model.Number;
import com.nidotim.lottery.model.Ticket;
import com.nidotim.lottery.model.enums.GameStatus;
import com.nidotim.lottery.repository.GameRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameScanService {

  private final GameRepository gameRepository;
  private final TicketService ticketService;
  private final NumberService numberService;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Game startScanGame(String id) {
    Game game = gameRepository.findById(id).get();
    game.setStatus(GameStatus.Scanning);
    return gameRepository.save(game);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Game endScanGame(String id) {
    Game game = gameRepository.findById(id).get();
    game.setStatus(GameStatus.Closed);
    game.setNumOfTickets(game.getTickets().size());
    return gameRepository.save(game);
  }

  public Page<Ticket> findUnscannedTickets(String id, Pageable pageable) {
    return ticketService.findUnscannedTickets(id, pageable);
  }

  @Transactional(readOnly = true)
  public Set<Integer> getWinningNumbers(String id) {
    Game game = gameRepository.findById(id).get();
    return game.getNumbers();
  }

  @Transactional(readOnly = true)
  public int getLeastWinningNumber(String id) {
    Game game = gameRepository.findById(id).get();
    return game.getLottery().getLeastWinningNumber();
  }

  @Transactional
  public Game scanTickets(Game game) {
    List<Ticket> tickets = game.getTickets();
    Set<Integer> winningNumbers = game.getNumbers();
    int leastWinningNumber = game.getLottery().getLeastWinningNumber();
    for(Ticket ticket : tickets) {
      if(ticket.isScanned()) {
        continue;
      }
      List<Number> numbers = numberService.getNumbers(ticket.getId());
      boolean win = false;
      for(Number number : numbers) {
        if(number.isScanned()) {
          continue;
        }
        List<Integer> ticketNumbers = number.getNumbers();
        int matchCount = 0;
        for(Integer ticketNumber : ticketNumbers) {
          if(winningNumbers.contains(ticketNumber)) {
            matchCount++;
          }
        }
        if(matchCount >= leastWinningNumber) {
          number.setWin(true);
          win = true;
        }
        number.setMatched(matchCount);
        number.setScanned(true);
        numberService.save(number);
      }
      ticket.setScanned(true);
      ticket.setWin(win);
      ticketService.save(ticket);
    }
    return game;
  }

  public void scanTickets(List<Ticket> tickets, Set<Integer> winningNumbers,
      int leastWinningNumber) {
    for (Ticket ticket : tickets) {
      if (ticket.isScanned()) {
        continue;
      }
      //log.debug("scan ticket. ticketId:{} ", ticket.getId());
      List<Number> numbers = numberService.getNumbers(ticket.getId());
      boolean win = false;
      for (Number number : numbers) {
        if (number.isScanned()) {
          continue;
        }
        List<Integer> ticketNumbers = number.getNumbers();
        int matchCount = 0;
        for (Integer ticketNumber : ticketNumbers) {
          if (winningNumbers.contains(ticketNumber)) {
            matchCount++;
          }
        }
        if (matchCount >= leastWinningNumber) {
          number.setWin(true);
          win = true;
        }
        number.setMatched(matchCount);
        number.setScanned(true);
        numberService.save(number);
      }
      ticket.setScanned(true);
      ticket.setWin(win);
      ticketService.save(ticket);
    }
  }
}
