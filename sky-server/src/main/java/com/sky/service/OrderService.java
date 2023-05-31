package com.sky.service;


import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;
import com.sky.entity.Orders;
import com.sky.entity.OrderDetail;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;


//@Mapper

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

}
