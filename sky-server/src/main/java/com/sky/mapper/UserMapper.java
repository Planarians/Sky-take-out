package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Select("select * from sky_take_out.user where openid= #{openid}")
    User getByOpenid(String openid);

//    // 注册
//    void insert(User user);
}
