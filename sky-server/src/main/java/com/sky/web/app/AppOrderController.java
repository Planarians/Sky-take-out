package com.sky.web.app;


import com.baomidou.mybatisplus.extension.api.R;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;


@Slf4j
@RestController
@RequestMapping("/user/order")
public class AppOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtProperties jwtProperties;

//    @Autowired
//    private
//

//
//    @GetMapping("/historyOrders")
//    public Result get{
//
//    }
//


    //提交订单
    @PostMapping("/submit")
    public Result submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }


    // 订单支付
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    @GetMapping("/historyOrders")
    public Result getPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.getPage(ordersPageQueryDTO);

        return Result.success(pageResult);
    }


    @GetMapping("/orderDetail/{id}")
    public Result getOrderDetail(@PathVariable("id") Long id) {

        OrderVO orderVO = orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }


    @PutMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable("id") Long id){
        orderService.cancelOrder(id);
        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable("id")Long id){
        orderService.repetition(id);
        return Result.success();
    }

    // 用户催单
    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable("id")Long id){
        orderService.reminder(id);
        return Result.success();
    }



}
