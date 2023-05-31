package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    //用户提交order
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        Random random = new Random();


        Long addressbookId = ordersSubmitDTO.getAddressBookId();

        AddressBook addressBook = addressBookMapper.selectById(addressbookId);
        if (addressBook == null) {
            throw new BusinessException(400, "请选择正确收货地址下单");
        }

        Orders orders = new Orders();
        BeanUtil.copyProperties(ordersSubmitDTO, orders);
        BeanUtil.copyProperties(addressBook, orders, "id", "creatTime");
        orders.setAmount(ordersSubmitDTO.getAmount().add(BigDecimal.valueOf(ordersSubmitDTO.getPackAmount())));
        orders.setOrderTime(LocalDateTime.now());
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setId((long) random.nextInt(1000000000));
        orderMapper.insert(orders);

        OrderDetail orderDetail = new OrderDetail();
        BeanUtil.copyProperties(orders, orderDetail);
        orderDetail.setOrderId(orders.getId());
        OrderDetail orderDetail2 = orderDetail;

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getByUserId(orders.getUserId());

        if (CollUtil.isEmpty(shoppingCartList)) {
            throw new BusinessException(400, "请选择菜品后进行下单");
        }
        for (ShoppingCart shoppingCart : shoppingCartList) {
            //if (shoppingCart.getDishId().equals(null)) ;

            OrderDetail orderDetail1 = orderDetail;
            BeanUtil.copyProperties(shoppingCart, orderDetail1);
            orderDetailMapper.insert(orderDetail1);
            orderDetail2 = orderDetail1;
            shoppingCartMapper.deleteById(shoppingCart);
        }
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        BeanUtil.copyProperties(orderDetail2, orderSubmitVO);


        return orderSubmitVO;
    }

//    @Override
//    public int insert(Order entity) {
//        return 0;
//    }
//
//    @Override
//    public int deleteById(Serializable id) {
//        return 0;
//    }
//
//    @Override
//    public int deleteByMap(Map<String, Object> columnMap) {
//        return 0;
//    }
//
//    @Override
//    public int delete(Wrapper<Order> queryWrapper) {
//        return 0;
//    }
//
//    @Override
//    public int deleteBatchIds(Collection<? extends Serializable> idList) {
//        return 0;
//    }
//
//    @Override
//    public int updateById(Order entity) {
//        return 0;
//    }
//
//    @Override
//    public int update(Order entity, Wrapper<Order> updateWrapper) {
//        return 0;
//    }
//
//    @Override
//    public Order selectById(Serializable id) {
//        return null;
//    }
//
//    @Override
//    public List<Order> selectBatchIds(Collection<? extends Serializable> idList) {
//        return null;
//    }
//
//    @Override
//    public List<Order> selectByMap(Map<String, Object> columnMap) {
//        return null;
//    }
//
//    @Override
//    public Order selectOne(Wrapper<Order> queryWrapper) {
//        return null;
//    }
//
//    @Override
//    public Integer selectCount(Wrapper<Order> queryWrapper) {
//        return null;
//    }
//
//    @Override
//    public List<Order> selectList(Wrapper<Order> queryWrapper) {
//        return null;
//    }
//
//    @Override
//    public List<Map<String, Object>> selectMaps(Wrapper<Order> queryWrapper) {
//        return null;
//    }
//
//    @Override
//    public List<Object> selectObjs(Wrapper<Order> queryWrapper) {
//        return null;
//    }
//
//    @Override
//    public <E extends IPage<Order>> E selectPage(E page, Wrapper<Order> queryWrapper) {
//        return null;
//    }
//
//    @Override
//    public <E extends IPage<Map<String, Object>>> E selectMapsPage(E page, Wrapper<Order> queryWrapper) {
//        return null;
//    }
}
