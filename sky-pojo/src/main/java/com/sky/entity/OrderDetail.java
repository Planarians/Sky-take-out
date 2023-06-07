package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GenerationType;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@javax.persistence.Table(name = "order_detail")
@javax.persistence.Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Column(name = "id", nullable = false)
    @javax.persistence.Id
    private Long id;

    //名称
    @javax.persistence.Column(name = "name")
    private String name;

    //订单id
    @javax.persistence.Column(name = "order_id", nullable = false)
    private Long orderId;

    //菜品id
    @javax.persistence.Column(name = "dish_id")
    private Long dishId;

    //套餐id
    @javax.persistence.Column(name = "setmeal_id")
    private Long setmealId;

    //口味
    @javax.persistence.Column(name = "dish_flavor")
    private String dishFlavor;

    //数量
    @javax.persistence.Column(name = "number", nullable = false)
    private Integer number;

    //金额
    @javax.persistence.Column(name = "amount", nullable = false)
    private BigDecimal amount;

    //图片
    @javax.persistence.Column(name = "image")
    private String image;
}
