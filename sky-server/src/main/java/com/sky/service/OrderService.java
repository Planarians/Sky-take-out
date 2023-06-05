package com.sky.service;


import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;

import com.sky.vo.OrderVO;
import lombok.Value;


//@Mapper

public interface OrderService {
    PageResult getPage(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;


    OrderVO getOrderDetail(Long id);

    void cancelOrder(Long id);

    void repetition(Long id);

    PageResult getAdmainPage(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO getStatistics();

    void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);

    void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO);

    void cancelOrderAdmin(OrdersCancelDTO ordersCancelDTO);

    void deliveryOrder(Long id);

    void completeOrder(Long id);


}
