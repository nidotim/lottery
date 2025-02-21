package com.nidotim.lottery.service;

import com.nidotim.lottery.model.User;
import com.nidotim.lottery.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final GameService gameService;

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public void buyTicket(String id, String gameId, List<Integer> numberList) {
    Optional<User> user = userRepository.findById(id);
    if(user.isPresent()) {
      gameService.buyTicket(user.get(), gameId, numberList);
    }
  }

  @Transactional
  public void buyTicket(String id, String gameId, Integer numOfTickets) {
    Optional<User> user = userRepository.findById(id);
    if(user.isPresent()) {
      int batchNum = 2000;
      int numbers = numOfTickets / batchNum;
      for(int i = 0; i < numbers; i++) {
        gameService.buyTicket(user.get(), gameId, batchNum);
      }
      int remainder = numOfTickets % batchNum;
      gameService.buyTicket(user.get(), gameId, remainder);

    }
  }

}
