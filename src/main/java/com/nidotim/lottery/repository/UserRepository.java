package com.nidotim.lottery.repository;

import com.nidotim.lottery.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

  @Override
  List<User> findAll();

}
