package com.nidotim.lottery.repository;

import com.nidotim.lottery.model.Lottery;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryRepository  extends CrudRepository<Lottery, String> {

  @Override
  List<Lottery> findAll();

}
