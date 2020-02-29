package com.nidotim.lottery.controller;

import com.nidotim.lottery.model.Game;
import com.nidotim.lottery.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lotteries")
@RequiredArgsConstructor
public class GameController {

  private final GameService gameService;

  @PutMapping("/{id}/rescan")
  public ResponseEntity<?> rescanGame(@PathVariable("id") String id) {
    Game game = gameService.getOne(id);
    gameService.scan(game.getId());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);

  }

}