package com.nidotim.lottery.repository;

import com.nidotim.lottery.model.LotteryHistory;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryHistoryRepository extends CrudRepository<LotteryHistory, String> {

  @Override
  List<LotteryHistory> findAll();

}
