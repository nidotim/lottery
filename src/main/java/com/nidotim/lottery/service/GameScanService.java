package com.nidotim.lottery.service;

import com.nidotim.lottery.model.Game;
import com.nidotim.lottery.model.Number;
import com.nidotim.lottery.model.Ticket;
import com.nidotim.lottery.model.enums.GameStatus;
import com.nidotim.lottery.repository.GameRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameScanService {

  private final GameRepository gameRepository;
  private final TicketService ticketService;
  private final NumberService numberService;

  @Transactional
  public Game startScanGame(Game game) {
    game.setStatus(GameStatus.Scanning);
    return gameRepository.save(game);
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
      List<Number> numbers = ticket.getNumberList();
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
        //numberService.save(number);
      }
      ticket.setScanned(true);
      ticket.setWin(win);
      ticketService.save(ticket);
    }
    return game;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Game endScanGame(Game game) {
    game.setStatus(GameStatus.Closed);
    return gameRepository.save(game);
  }
}
