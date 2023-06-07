package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GenerationType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@javax.persistence.Table(name = "orders")
@javax.persistence.Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     * 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer CONFIRMED = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED = 5;
    public static final Integer CANCELLED = 6;

    /**
     * 支付状态 0未支付 1已支付 2已退款
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    private static final long serialVersionUID = 1L;

    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Column(name = "id", nullable = false)
    @javax.persistence.Id
    private Long id;

    //订单号
    @javax.persistence.Column(name = "number")
    private String number;

    //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款
    @javax.persistence.Column(name = "status", nullable = false)
    private Integer status;

    //下单用户id
    @javax.persistence.Column(name = "user_id", nullable = false)
    private Long userId;

    //地址id
    @javax.persistence.Column(name = "address_book_id", nullable = false)
    private Long addressBookId;

    //下单时间
    @javax.persistence.Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    //结账时间
    @javax.persistence.Column(name = "checkout_time")
    private LocalDateTime checkoutTime;

    //支付方式 1微信，2支付宝
    @javax.persistence.Column(name = "pay_method", nullable = false)
    private Integer payMethod;

    //支付状态 0未支付 1已支付 2退款
    @javax.persistence.Column(name = "pay_status", nullable = false)
    private Integer payStatus;

    //实收金额
    @javax.persistence.Column(name = "amount", nullable = false)
    private BigDecimal amount;

    //备注
    @javax.persistence.Column(name = "remark")
    private String remark;

    //用户名
    private String userName;

    //手机号
    @javax.persistence.Column(name = "phone")
    private String phone;

    //地址
    @javax.persistence.Column(name = "address")
    private String address;

    //收货人


    @javax.persistence.Column(name = "consignee")
    private String consignee;

    //订单取消原因
    @javax.persistence.Column(name = "cancel_reason")
    private String cancelReason;

    //订单拒绝原因
    @javax.persistence.Column(name = "rejection_reason")
    private String rejectionReason;

    //订单取消时间
    @javax.persistence.Column(name = "cancel_time")
    private LocalDateTime cancelTime;

    //预计送达时间
    @javax.persistence.Column(name = "estimated_delivery_time")
    private LocalDateTime estimatedDeliveryTime;

    //配送状态  1立即送出  0选择具体时间
    @javax.persistence.Column(name = "delivery_status", nullable = false)
    private Integer deliveryStatus;

    //送达时间
    @javax.persistence.Column(name = "delivery_time")
    private LocalDateTime deliveryTime;

    //打包费
    @javax.persistence.Column(name = "pack_amount")
    private int packAmount;

    //餐具数量
    @javax.persistence.Column(name = "tableware_number")
    private int tablewareNumber;

    //餐具数量状态  1按餐量提供  0选择具体数量
    @javax.persistence.Column(name = "tableware_status", nullable = false)
    private Integer tablewareStatus;
}
