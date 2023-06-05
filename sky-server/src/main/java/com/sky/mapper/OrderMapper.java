package com.sky.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Param;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.sky.entity.Orders;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderService;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;




@Mapper
public interface OrderMapper extends BaseMapper<Orders> {


    @Select("select * from sky_take_out.orders where number=#{orderNumber}")
    Orders selectByNumber(String orderNumber);

    @Select("select * from sky_take_out.orders where user_id =#{userId}")
    List<Orders> getByUserId(@Param("userId") Long userId);

    @Select("select * from sky_take_out.orders")
    List<Orders> getAll(QueryWrapper<OrderVO> queryWrapper);

    @Select("select * from sky_take_out.orders where id =#{id}")
    Orders getById(Long id);

//    @Select("select count(*) from sky_take_out.orders where status =#{status}")
//    Integer getStatusNumber(Integer status);

    @Select("select count(*) from sky_take_out.orders where status = #{status}")
    Integer getStatusNumber(Integer status) ;

    @Select("select count(*) from sky_take_out.orders where status = #{status}")
    Integer countStatus(Integer status);

    // 修改订单状态
    @Select("select * from sky_take_out.orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrdertimeLT(Integer status, LocalDateTime orderTime);

}
