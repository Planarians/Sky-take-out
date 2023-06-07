package com.sky.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.util.StringUtil;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.repository.OrderDetailRepository;
import com.sky.repository.OrdersRepository;
import com.sky.repository.UserRepository;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;

import java.time.LocalTime;
import java.util.*;
import java.time.LocalDate;


@Service
public class ReportServiceImpl implements ReportService {


    @Autowired(required = false)
    OrderMapper orderMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;


    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> LocalDateList = new ArrayList<>();
        LocalDate k = begin;
        while (!begin.equals(end)) {
            LocalDateList.add(begin);
            begin = begin.plusDays(1);
        }
        return LocalDateList;
    }

    class ValueComparator implements Comparator<String> {
        Map<String, Integer> map;

        public ValueComparator(Map<String, Integer> map) {
            this.map = map;
        }

        @Override
        public int compare(String s1, String s2) {
            int value1 = map.get(s1);
            int value2 = map.get(s2);
            return value2 - value1;
        }
    }


    @Override
    public TurnoverReportVO getStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> LocalDateTimeList = getDateList(begin, end);
        List<BigDecimal> orderAmountList = new ArrayList<>();
        for (LocalDate currentDate : LocalDateTimeList) {
            BigDecimal amount = orderMapper.getByStatusAndOrdertimeBetween(currentDate);
            if (amount == null) {
                amount = BigDecimal.ZERO;
            }
            orderAmountList.add(amount);
        }
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        turnoverReportVO.setDateList(StrUtil.join(",", LocalDateTimeList));
        turnoverReportVO.setTurnoverList(StrUtil.join(",", orderAmountList));
        return turnoverReportVO;
    }


    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);
        List<Long> newUserList = new ArrayList<>();
        List<Long> totalUserList = new ArrayList<>();

        for (LocalDate localDate : dateList) {
            Long userNumber = userMapper.countUserIdByCreateTime_Date(localDate);
            if (userNumber == null) {
                userNumber = 0L;
            }
            newUserList.add(userNumber);
            for (int i = 0; i < 1; i++) {
                totalUserList.add(userNumber);
            }
            int size = totalUserList.size();
            totalUserList.add(userNumber + totalUserList.get(size - 1));
        }


        UserReportVO userReportVO = new UserReportVO();
        userReportVO.setDateList(StrUtil.join(",", dateList));
        userReportVO.setTotalUserList(StrUtil.join(",", totalUserList));
        userReportVO.setNewUserList(StrUtil.join(",", newUserList));


        return userReportVO;
    }

    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = getDateList(begin, end);

        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        Integer validOrderCount = orderRepository.countOrderIdsByStatus(5);
        Integer totalOrderCount = orderRepository.countOrdersIdBy();
        Double orderCompletionRate = validOrderCount * 1.0 / totalOrderCount;


        for (LocalDate localDate : dateList) {
            // localDate=localDate.atStartOfDay().toLocalDate();
            orderCountList.add(orderRepository.countByOrderTime(localDate));
            orderCountList.add(orderRepository.countByOrderTime(localDate));

            validOrderCountList.add(orderRepository.countByStatusAndOrderTime(5, localDate));
        }


        return OrderReportVO.builder().
                dateList(StrUtil.join(",", dateList))
                .orderCountList(StrUtil.join(",", orderCountList))
                .validOrderCountList(StrUtil.join(",", validOrderCountList))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate).
                build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        List<LocalDate> dateList = getDateList(begin, end);
        Map<String, Integer> SalesMap = new HashMap<>();


        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        class orderDetilemini{
            Integer orderNumber;
            String orderName;
        }
        List<orderDetilemini> orderDetileminiList=new ArrayList<>();



        Integer orderNumber = 0;
        for (LocalDate localDate : dateList) {
            List<Orders> ordersList = orderRepository.getByStatusAndOrderTime(5,localDate);
            for (Orders orders : ordersList) {
                //orders.getId();
                List<OrderDetail> orderDetailList = orderDetailRepository.getByOrderId(orders.getId());
                for (OrderDetail orderDetail : orderDetailList) {
                    if (SalesMap.containsKey(orderDetail.getName())) {
                        orderNumber = SalesMap.get(orderDetail.getName()) + orderDetail.getNumber();
                    } else {
                        orderNumber = orderDetail.getNumber();
                    }

                    // SalesMap.put(orderDetail.getName());
                    SalesMap.put(orderDetail.getName(), orderNumber);


                    orderNumber = 0;
                }
            }


        }

        SalesMap.forEach((k, v) ->{
            orderDetilemini orderDetileminii= new orderDetilemini();
            orderDetileminii.setOrderName(k);
            orderDetileminii.setOrderNumber(v);
            orderDetileminiList.add(orderDetileminii);
        } );
//        orderDetilemini orderDetileminii= new orderDetilemini();
//        orderDetileminii.setOrderName(orderDetail.getName());
//        orderDetileminii.setOrderNumber(orderNumber);
//        orderDetileminiList.add(orderDetileminii);

        Collections.sort(orderDetileminiList,new Comparator<orderDetilemini>(){

            @Override
            public int compare(orderDetilemini o1, orderDetilemini o2) {
//                if(o2.getOrderNumber()>=o1.getOrderNumber()){
//                    return 1;
//                }else{
//                    return -1;
//                }
                return  o2.getOrderNumber()- o1.getOrderNumber();
            }

        });

        int k=10;
        for (orderDetilemini orderDetileminii : orderDetileminiList) {
            nameList.add(orderDetileminii.getOrderName());
            numberList.add(orderDetileminii.getOrderNumber());
//            numberList.add(entry.getValue());
            k--;
            if(k<=0){break;}
        }
//        SalesMap.entrySet().stream().limit(10).forEach(entry -> {
//            nameList.add(entry.getKey());
//            numberList.add(entry.getValue());
//
//        });


        return SalesTop10ReportVO.builder()
                .nameList(StrUtil.join(",", nameList))
                .numberList(StrUtil.join(",", numberList))
                .build();
    }


//
//    @Override
//    public TurnoverReportVO getStatistics(LocalDateTime begin, LocalDateTime end) {
//        return null;
//    }
}
