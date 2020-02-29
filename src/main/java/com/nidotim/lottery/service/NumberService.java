package com.nidotim.lottery.service;

import com.nidotim.lottery.model.Number;
import com.nidotim.lottery.repository.NumberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NumberService {

  private final NumberRepository numberRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Number save(Number number) {
    return numberRepository.save(number);
  }


  public List<Number> getNumbers(String ticketId) {
    return numberRepository.findByTicketIdIs(ticketId);
  }
//
//  @Transactional(propagation = Propagation.REQUIRES_NEW)
//  public List<Number> saveAll(List<Number> numbers) {
//    return numberRepository.saveAll(numbers);
//  }

}
