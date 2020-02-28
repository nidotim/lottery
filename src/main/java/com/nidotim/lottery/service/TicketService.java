package com.nidotim.lottery.service;

import com.nidotim.lottery.model.Ticket;
import com.nidotim.lottery.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;

  @Transactional
  public Ticket save(Ticket ticket) {
    return ticketRepository.save(ticket);
  }

}
