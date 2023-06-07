package com.sky.repository;

import com.sky.entity.Dish;
import com.sky.entity.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Integer countOrderIdsByStatus(Integer status);

    Integer countOrdersIdBy();

   // Integer countByOrderTime_Date(LocalDate localDate);
    Integer countByOrderTimeBetween(LocalDateTime begin,LocalDateTime end);

    default Integer countByOrderTime(LocalDate date) {
        LocalDateTime begin = date.atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return countByOrderTimeBetween(begin, end);
    }

   // Integer countByStatusAndOrderTimeEquals(Integer status,LocalDate localDate);
    Integer countByStatusAndOrderTimeBetween(Integer status,LocalDateTime begin,LocalDateTime end);


    default Integer countByStatusAndOrderTime(Integer status,LocalDate date) {
        LocalDateTime begin = date.atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return countByStatusAndOrderTimeBetween(status,begin, end);
    }





    List<Orders> getByStatusAndOrderTimeBetween(Integer status,LocalDateTime begin, LocalDateTime end);

    default List<Orders> getByStatusAndOrderTime(Integer status,LocalDate date) {
        LocalDateTime begin = date.atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return getByStatusAndOrderTimeBetween(status,begin, end);
    }


}
