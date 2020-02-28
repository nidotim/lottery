package com.nidotim.lottery.util;

import com.nidotim.lottery.model.Lottery;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class LotteryGenerator {

  private Random random;

  private int minNum;

  private int maxNum;

  private int numOfNumbers;

  public LotteryGenerator(Lottery lottery) {
    this.random = new Random();
    if(lottery == null) {
      return;
    }
    minNum  = lottery.getMinNum();
    maxNum = lottery.getMaxNum();
    numOfNumbers = lottery.getNumOfNumbers();
  }

  public Set<Integer> generateNumbers() {
    Set<Integer> numbers = new TreeSet<>();
    while (numbers.size() < numOfNumbers) {
      numbers.add(this.random.nextInt((maxNum - minNum) + 1) + minNum);
    }
    return numbers;
  }
}
