package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderOverViewVO;
import org.springframework.stereotype.Service;



public interface WorkspaceService {
    BusinessDataVO getTodayBussinessDate() ;

    OrderOverViewVO getOverViewOrders();
}
