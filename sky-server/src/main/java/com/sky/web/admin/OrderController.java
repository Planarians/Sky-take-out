package com.sky.web.admin;


import com.baomidou.mybatisplus.extension.api.R;
import com.sky.dto.*;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;


@Slf4j
@RestController
@RequestMapping("/admin/order")
public class OrderController {

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


//    //提交订单
//    @PostMapping("/submit")
//    public Result submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
//        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
//        return Result.success(orderSubmitVO);
//    }
//
//
//    // 订单支付
//    @PutMapping("/payment")
//    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
//        log.info("订单支付：{}", ordersPaymentDTO);
//        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
//        log.info("生成预支付交易单：{}", orderPaymentVO);
//        return Result.success(orderPaymentVO);
//    }

    @GetMapping("/conditionSearch")
    public Result getAdmainPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.getAdmainPage(ordersPageQueryDTO);

        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.getStatistics();

        return Result.success(orderStatisticsVO);
    }



    @GetMapping("/details/{id}")
    public Result getAdmianOrderDetail(@PathVariable("id") Long id) {

        OrderVO orderVO = orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }


    @PutMapping("/confirm")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success();
    }


    @PutMapping("/rejection")
    public Result rejectionOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        orderService.rejectionOrder(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO){
        orderService.cancelOrderAdmin(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    public Result deliveryOrder(@PathVariable("id") Long id){
        orderService.deliveryOrder(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable("id") Long id){
        orderService.completeOrder(id);
        return Result.success();
    }


//
//    @PostMapping("/repetition/{id}")
//    public Result repetition(@PathVariable("id")Long id){
//        orderService.repetition(id);
//        return Result.success();
//    }





}
