package com.sky.service.impl;

import cn.hutool.core.util.StrUtil;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


@Service
public class ReportServiceImpl implements ReportService {


    @Autowired(required = false)
    OrderMapper orderMapper;



    @Override
    public TurnoverReportVO getStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> LocalDateTimeList = new ArrayList<>();

        LocalDate k = begin;


        while (!begin.equals(end)) {
            LocalDateTimeList.add(begin);
            begin = begin.plusDays(1);

        }


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
//
//    @Override
//    public TurnoverReportVO getStatistics(LocalDateTime begin, LocalDateTime end) {
//        return null;
//    }
}
