package com.sky.web.admin;


import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/workspace")
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/businessData")
    public Result getTodayBussinessDate(){

        BusinessDataVO businessDataVO= workspaceService.getTodayBussinessDate();

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


}
