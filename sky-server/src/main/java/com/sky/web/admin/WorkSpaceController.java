package com.sky.web.admin;


import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("admin/workspace")
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

//    @GetMapping("/businessData")
//    public Result getTodayBussinessDate(){
//
//        BusinessDataVO businessDataVO= workspaceService.getTodayBussinessDate();
//
//        return Result.success(businessDataVO);
//    }

    /**
     * 工作台今日数据查询
     * @return
     */
    @GetMapping("/businessData")
    public Result getBusinessData() {
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now();
        BusinessDataVO businessDataVO = workspaceService.getBusinessData1(begin,end);
        return Result.success(businessDataVO);
    }

    @GetMapping("/overviewOrders")
    public Result getOverViewOrders(){

        OrderOverViewVO orderOverViewVO = workspaceService.getOverViewOrders();

        return Result.success(orderOverViewVO);
    }
//
//    @GetMapping("/overviewOrders")
//    public Result getOverViewOrders(){
//
//        OrderOverViewVO orderOverViewVO = workspaceService.getOverViewOrders();
//
//        return Result.success(orderOverViewVO);
//    }

    // 查询菜品总览
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> dishOverView(){
        return Result.success(workspaceService.getDishOverView());
    }

    // 查询套餐总览
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> setmealOverView(){
        return Result.success(workspaceService.getSetmealOverView());
    }
}
