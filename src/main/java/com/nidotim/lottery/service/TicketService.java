package com.nidotim.lottery.service;

import com.nidotim.lottery.model.Ticket;
import com.nidotim.lottery.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;


  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Ticket save(Ticket ticket) {
    return ticketRepository.save(ticket);
  }

  public Page<Ticket> findUnscannedTickets(String gameId, Pageable pageable) {
    return ticketRepository.findByGameIdIsAndScannedIsFalse(gameId, pageable);
  }
}
