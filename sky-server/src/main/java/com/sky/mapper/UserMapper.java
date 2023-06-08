package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;


@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Select("select * from sky_take_out.user where openid= #{openid}")
    User getByOpenid(String openid);


    @Select( "select count(id) from sky_take_out.user where date(create_time) = #{localDate}")
    Long countUserIdByCreateTime_Date(LocalDate localDate);

    @Select("select count(*) from sky_take_out.user where date(create_time) between #{begin} and #{end}")
    Integer getNewUserCount1(LocalDate begin, LocalDate end);


//    // 注册
//    void insert(User user);
}
