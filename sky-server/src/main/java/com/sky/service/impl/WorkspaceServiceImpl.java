package com.sky.service.impl;

import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.repository.OrdersRepository;
import com.sky.repository.UserRepository;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReportServiceImpl reportService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> LocalDateList = new ArrayList<>();
        LocalDate k = begin;
        while (!begin.equals(end)) {
            LocalDateList.add(begin);
            begin = begin.plusDays(1);
        }
        return LocalDateList;
    }

    @Override
    public BusinessDataVO getTodayBussinessDate() {


        //LocalDate date = LocalDate.now();
        LocalDate date = LocalDate.of(2023, 5, 31);
        LocalDate beginTime = date;
        LocalDate endTime = date;
        TurnoverReportVO statistics = reportService.getStatistics(beginTime, endTime);
        String turnoverList = statistics.getTurnoverList();
        BigDecimal turnoverBig = orderMapper.getByStatusAndOrdertimeBetween(date);
        Double turnover = turnoverBig.doubleValue();
        OrderReportVO orderStatistics = reportService.getOrderStatistics(beginTime, endTime);
        Integer validOrderCount = orderStatistics.getValidOrderCount();
        Integer total = orderStatistics.getTotalOrderCount();
        Double orderCompletionRate = orderStatistics.getOrderCompletionRate();
        UserReportVO userStatistics = reportService.getUserStatistics(beginTime, endTime);
        String totalUserList = userStatistics.getTotalUserList();

        Long newUsers = userMapper.countUserIdByCreateTime_Date(date);


        String newUserList = userStatistics.getNewUserList();
        //Integer newUsers = Integer.parseInt(newUserList);
        Double unitPrice = turnover / validOrderCount;


        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(Math.toIntExact(newUsers))
                .total(total)
                .date(date)
                .beginTime(beginTime)
                .endTime(endTime)
                .build();
    }

    @Override
    public OrderOverViewVO getOverViewOrders() {

//        LocalDate localDate=LocalDate.now();
        LocalDate localDate=LocalDate.of(2023,5,31);
        Integer cancelledOrders = ordersRepository.countByStatusAndOrderTime(6, localDate);
        Integer waitingOrders = ordersRepository.countByStatusAndOrderTime(2, localDate);
        Integer completedOrders = ordersRepository.countByStatusAndOrderTime(5, localDate);
        Integer deliveredOrders = ordersRepository.countByStatusAndOrderTime(3, localDate);
        Integer allOrders = ordersRepository.countByStatusAndOrderTime(null, localDate);



        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }
}
