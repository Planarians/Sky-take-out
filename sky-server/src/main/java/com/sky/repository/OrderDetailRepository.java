package com.sky.repository;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {


    List<OrderDetail> getByOrderId(Long id);

//    getByOrderByIdAndDetailTimeBetween
//    OrderDetail getByIdAndOrder
//            (LocalDateTime begin, LocalDateTime end);
//
//    default OrderDetail getByOrderDetailTime(LocalDate date) {
//        LocalDateTime begin = date.atStartOfDay();
//        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
//        return getByOrderDetailTimeBetween(begin, end);
//    }

}
