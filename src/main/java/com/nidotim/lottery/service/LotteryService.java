package com.nidotim.lottery.service;

import com.nidotim.lottery.model.Game;
import com.nidotim.lottery.model.Lottery;
import com.nidotim.lottery.model.LotteryHistory;
import com.nidotim.lottery.repository.LotteryHistoryRepository;
import com.nidotim.lottery.repository.LotteryRepository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LotteryService {

  private final LotteryRepository lotteryRepository;
  private final GameService gameService;
  private final LotteryHistoryRepository lotteryHistoryRepository;

  public Lottery createLottery(Lottery lottery) {
    return lotteryRepository.save(lottery);
  }

  public List<Lottery> findAllLotteries() {
    return lotteryRepository.findAll();
  }

  public Game startGame(String id) {
    Optional<Lottery> lottery = lotteryRepository.findById(id);

    if (lottery.isPresent()) {
      return gameService.startGame(lottery.get());
    }
    return null;
  }

  public Game getCurrentGame(String id) {
    Optional<Lottery> lottery = lotteryRepository.findById(id);

    if (lottery.isPresent()) {
      return gameService.getCurrentGame(lottery.get());
    }
    return null;
  }

  @Transactional
  public void parseHistory(String id) {
    Optional<Lottery> lotteryOpt = lotteryRepository.findById(id);

    if (!lotteryOpt.isPresent()) {
      return;
    }
    DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
    Lottery lottery = lotteryOpt.get();
    try (BufferedReader br = new BufferedReader(
        new FileReader("./src/main/resources/LottoMaxHistory.csv"))) {

      List<LotteryHistory> lotteryHistories = new ArrayList<>();
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(";");
        Date date = df.parse(values[0]);
        Instant instant = date.toInstant();
        List<Integer> numbers = Arrays.asList(
            Integer.parseInt(values[1]),
            Integer.parseInt(values[2]),
            Integer.parseInt(values[3]),
            Integer.parseInt(values[4]),
            Integer.parseInt(values[5]),
            Integer.parseInt(values[6]),
            Integer.parseInt(values[7]));
        LotteryHistory lotteryHistory = LotteryHistory.builder()
            .lottery(lottery)
            .numbers(numbers)
            .date(instant)
            .build();
        lotteryHistoryRepository.save(lotteryHistory);
      }
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
  }

}
