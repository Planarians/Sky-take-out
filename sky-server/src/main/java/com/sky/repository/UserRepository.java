package com.sky.repository;

import com.sky.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserBySex(String sex);


    Optional<User> findByOpenid(String openid);

    User findUserByOpenid(String openid);

//
//    @Query("select count(id) from user where date(create_time)=:localDate")
//    Long countUserIdByCreateTime(LocalDate localDate);
}
