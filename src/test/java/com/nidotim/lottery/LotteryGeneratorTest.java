package com.nidotim.lottery;

import com.nidotim.lottery.model.Lottery;
import com.nidotim.lottery.util.LotteryGenerator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class LotteryGeneratorTest {

  private Lottery getLottoMaxLottery() {
    return Lottery.builder()
        .minNum(1)
        .maxNum(50)
        .numOfNumbers(7)
        .name("Lotto Max")
        .numOfNumbersInTicket(1)
        .leastWinningNumber(3)
        .build();
  }

  private Lottery get649Lottery() {
    return Lottery.builder()
        .minNum(1)
        .maxNum(49)
        .numOfNumbers(6)
        .name("Lotto 649")
        .numOfNumbersInTicket(1)
        .leastWinningNumber(3)
        .build();
  }

  @Test
  public void getALottoMaxNumber() {
    int runTimes = 71004556;
    Lottery lottoMax = getLottoMaxLottery();
    LotteryGenerator generator = new LotteryGenerator(lottoMax);
    generator.generateNumbers();
    Set<Integer> numbers = null;
    for (int i = 0; i < runTimes; i++) {
      numbers = generator.generateNumbers();
    }
    System.out.println(numbers);
  }

  @Test
  public void getA649Number() {
    int runTimes = 47856912;
    Lottery lottoMax = get649Lottery();
    LotteryGenerator generator = new LotteryGenerator(lottoMax);
    generator.generateNumbers();
    Set<Integer> numbers = null;
    for (int i = 0; i < runTimes; i++) {
      numbers = generator.generateNumbers();
    }
    System.out.println(numbers);
  }

  @Test
  public void findWinningNumber() {
    int count = 0;
    Set<Integer> winningNumber = new HashSet<>(Arrays.asList(21, 27, 30, 32, 33, 36, 46));
    Lottery lottoMax = getLottoMaxLottery();
    LotteryGenerator generator = new LotteryGenerator(lottoMax);
    while (true) {
      Set<Integer> numbers = generator.generateNumbers();
      count++;
      int matchCount = 0;
      for (Integer number : numbers) {
        if (winningNumber.contains(number)) {
          matchCount++;
        }
      }
      if (matchCount == 7) {
        break;
      }
    }
    System.out.println("=======> " + count);
  }
}
