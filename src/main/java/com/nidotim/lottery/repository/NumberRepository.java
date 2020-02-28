package com.nidotim.lottery.repository;

import com.nidotim.lottery.model.Lottery;
import com.nidotim.lottery.model.Number;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberRepository extends CrudRepository<Number, String> {

}
