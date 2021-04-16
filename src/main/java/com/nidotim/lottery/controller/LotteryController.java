package com.nidotim.lottery.controller;

import com.nidotim.lottery.model.Game;
import com.nidotim.lottery.model.Lottery;
import com.nidotim.lottery.service.LotteryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lotteries")
@RequiredArgsConstructor
public class LotteryController {

  private final LotteryService lotteryService;

  @PostMapping
  public ResponseEntity<?> createLottery(@RequestBody Lottery lottery) {
    lottery = lotteryService.createLottery(lottery);
    return new ResponseEntity<>(lottery, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<?> getLotteries() {
    List<Lottery> lotteryList = lotteryService.findAllLotteries();
    return new ResponseEntity<>(lotteryList, HttpStatus.OK);
  }

  @PostMapping("/{id}/start")
  public ResponseEntity<?> startGame(@PathVariable("id") String id) {
    Game game = lotteryService.startGame(id);
    if (game == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(game, HttpStatus.OK);
    }
  }

  @GetMapping("/{id}/games/current")
  public ResponseEntity<?> getCurrentGame(@PathVariable("id") String id) {
    Game game = lotteryService.getCurrentGame(id);
    if (game == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(game, HttpStatus.OK);
    }
  }

  @PostMapping("/{id}/parseHistory")
  public ResponseEntity<?> parseHistory(@PathVariable("id") String id) {
    lotteryService.parseHistory(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}