package com.sky.web.app;


import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
public class AppOrderController {

    @Autowired
    private OrderService orderService;


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

}
