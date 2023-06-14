package com.sky.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

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
        Long newUsers = userMapper.countUserIdByCreateTime_Date(date);


        String newUserList = userStatistics.getNewUserList();
       // Double turnover = Double.parseDouble(turnoverList);


        Double unitPrice = turnover / validOrderCount;

        //Double turnover = Double.parseDouble(turnoverList);

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

    // 查询套餐总览
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        // 统计起售套餐数量
        Integer sold = setmealMapper.countByStatus(StatusConstant.ENABLE);
        // 统计停售套餐数量
        Integer discontinued = setmealMapper.countByStatus(StatusConstant.DISABLE);
        // 返回结果
        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    @Override
    // 查询菜品总览
    public DishOverViewVO getDishOverView() {
        // 统计起售菜品数量
        Integer sold =  dishMapper.countByStatus(StatusConstant.ENABLE);
        // 统计停售菜品数量
        Integer discontinued =  dishMapper.countByStatus(StatusConstant.DISABLE);
        // 返回VO
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 今日数据
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData1(LocalDate begin, LocalDate end) {
        //总订单数
       // Integer total = orderMapper.orderCountByDate1(null, begin,end);
        Integer total=  ordersRepository.countByStatusAndOrderTimeBetween(null,begin.atStartOfDay(),end.atTime(LocalTime.MAX));
        //有效订单数
        Integer validOrderCount = ordersRepository.countByStatusAndOrderTimeBetween(5,begin.atStartOfDay(),end.atTime(LocalTime.MAX));
        //订单完成率
        Double orderCompletionRate = 0.0;
        if (total != 0 && validOrderCount != 0) {
            orderCompletionRate = NumberUtil.div(validOrderCount * 1.0, total * 1.0, 2, RoundingMode.HALF_UP);
        }
        //营业额
        BigDecimal sumTurnover = orderMapper.getSumTurnover1(begin,end);
        Double turnover = sumTurnover == null ? 0.0 : sumTurnover.doubleValue();

        Double unitPrice = 0.0;//平均客单价
        if (turnover != null && validOrderCount != 0) {
            unitPrice = NumberUtil.div(turnover * 1.0, validOrderCount * 1.0, 2, RoundingMode.HALF_UP);
        }
        //新增用户数
        Integer newUsers = userMapper.getNewUserCount1(begin,end);

        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .total(total)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .turnover(turnover == null ? 0 : turnover)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
        return businessDataVO;
    }
}
