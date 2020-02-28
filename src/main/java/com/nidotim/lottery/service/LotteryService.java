package com.nidotim.lottery.service;

import com.nidotim.lottery.model.Game;
import com.nidotim.lottery.model.Lottery;
import com.nidotim.lottery.repository.LotteryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LotteryService {

  private final LotteryRepository lotteryRepository;
  private final GameService gameService;

  public Lottery createLottery(Lottery lottery) {
    return lotteryRepository.save(lottery);
  }

  public List<Lottery> findAllLotteries() {
    return lotteryRepository.findAll();
  }

  public Game startGame(String id) {
    Optional<Lottery> lottery = lotteryRepository.findById(id);

    if(lottery.isPresent()) {
      return gameService.startGame(lottery.get());
    }
    return null;
  }

  public Game getCurrentGame(String id) {
    Optional<Lottery> lottery = lotteryRepository.findById(id);

    if(lottery.isPresent()) {
      return gameService.getCurrentGame(lottery.get());
    }
    return null;
  }
}
