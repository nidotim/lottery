package com.nidotim.lottery.repository;

import com.nidotim.lottery.model.Game;
import com.nidotim.lottery.model.Lottery;
import com.nidotim.lottery.model.enums.GameStatus;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, String> {

  @Override
  List<Game> findAll();

  boolean existsByLotteryIsAndStatusIs(Lottery lottery, GameStatus status);

  Game findByLotteryIsAndStatusIs(Lottery lottery, GameStatus status);

  Page<Game> findByStatusIsAndEndDateBefore(GameStatus status, Instant endTime, Pageable pageable);

  Page<Game> findByStatusIs(GameStatus status, Pageable pageable);

}
