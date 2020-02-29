package com.nidotim.lottery.repository;

import com.nidotim.lottery.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, String> {

  Page<Ticket> findByGameIdIsAndScannedIsFalse(String gameId, Pageable pageable);

}
