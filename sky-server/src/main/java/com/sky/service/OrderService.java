package com.sky.service;


import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.entity.Orders;
import com.sky.entity.OrderDetail;

import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;


//@Mapper

public interface OrderService {
    PageResult getPage(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;


    OrderVO getOrderDetail(Long id);

    void cancelOrder(Long id);

    void repetition(Long id);
}
