package com.nidotim.lottery.controller;

import com.nidotim.lottery.model.User;
import com.nidotim.lottery.service.UserService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody User user) {
    user = userService.createUser(user);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<?> getUsers() {
    List<User> userList = userService.findAllUsers();
    return new ResponseEntity<>(userList, HttpStatus.OK);
  }

  @PostMapping("/{id}/games/{gameId}")
  public ResponseEntity<?> buyTicket(@PathVariable("id") String id,
      @PathVariable("gameId") String gameId, @RequestBody List<Integer> numberList) {
    userService.buyTicket(id, gameId, numberList);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/{id}/games/{gameId}/tickets/{numOfTickets}")
  public ResponseEntity<?> buyTickets(@PathVariable("id") String id,
      @PathVariable("gameId") String gameId, @PathVariable("numOfTickets") Integer numOfTickets) {
    userService.buyTicket(id, gameId, numOfTickets);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}