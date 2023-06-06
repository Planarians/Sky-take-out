package com.sky.repository;

import com.sky.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserBySex(String sex);


  Optional  <User> findByOpenid(String openid);

  User findUserByOpenid(String openid);



}
