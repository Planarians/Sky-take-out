package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


public interface WorkspaceService {
    BusinessDataVO getTodayBussinessDate() ;

    OrderOverViewVO getOverViewOrders();

    // 查询套餐总览
    SetmealOverViewVO getSetmealOverView();

    // 查询菜品总览
    DishOverViewVO getDishOverView();

    BusinessDataVO getBusinessData1(LocalDate begin, LocalDate end);
}
