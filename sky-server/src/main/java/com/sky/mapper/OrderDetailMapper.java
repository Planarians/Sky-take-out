package com.sky.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {


    @Select("select * from sky_take_out.order_detail where order_id =#{orderId}")
    List<OrderDetail> selectByOrderId(@Param("orderId") Long orderId);
}
