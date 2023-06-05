package com.sky.task;

import com.sky.annotation.AutoFill;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    
    @Autowired
    private OrderMapper orderMapper;

    // 每分钟处理一次订单超时时间
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeOutOrder(){
        log.info("开始清理超时订单....");
        // 获取15分钟前的时间
        LocalDateTime old15Time = LocalDateTime.now().plusMinutes(-15);
        // 查询未支付订单
        List<Orders> weiZhiFulList = orderMapper.getByStatusAndOrdertimeLT(Orders.PENDING_PAYMENT, old15Time);
        // 遍历更改状态为取消
        weiZhiFulList.forEach(weiZhiFu->{
            weiZhiFu.setStatus(Orders.CANCELLED);
            weiZhiFu.setCancelTime(LocalDateTime.now());
            weiZhiFu.setCancelReason("用户超时未支付");
            orderMapper.update(weiZhiFu);
            log.info("清理超时订单号为："+weiZhiFu.getNumber());
        });
        log.info("结束清理超时订单....");
    }




    // 每天凌晨1点处理前一天的配送中订单
    @Scheduled(cron = "0 0 1 * * ?")
    public void processToOver(){
        // 获取1小时前的时间
        log.info("开始处理配送订单....");
        // 获取60分钟前的时间
        LocalDateTime old60Time = LocalDateTime.now().plusMinutes(-60);
        // 查询配送中订单
        List<Orders> peiSongZhongList = orderMapper.getByStatusAndOrdertimeLT(Orders.DELIVERY_IN_PROGRESS, old60Time);
        // 遍历更改状态为取消
        peiSongZhongList.forEach(peiSongZhong->{
            peiSongZhong.setStatus(Orders.COMPLETED);
            orderMapper.update(peiSongZhong);
            log.info("处理配送订单号为："+peiSongZhong.getNumber());
        });
        log.info("结束处理配送订单....");
    }
}