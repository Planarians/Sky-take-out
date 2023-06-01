package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.ThreadLocalUtil;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
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
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
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

    @Autowired
    private ShoppingCartService shoppingCartService;


    @Override
    public PageResult getPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        //Orders orders = orderMapper.selectById(ordersPageQueryDTO.get)
        ordersPageQueryDTO.setUserId(ThreadLocalUtil.getCurrentId());
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
      //  List<OrderVO> voList = orderMapper.getList(ordersPageQueryDTO);

        QueryWrapper<OrderVO> queryWrapper= new QueryWrapper<>();

        if(ordersPageQueryDTO.getNumber()!=null){
            queryWrapper.eq("number",ordersPageQueryDTO.getNumber());
        }
        if(ordersPageQueryDTO.getPhone()!=null) {
            queryWrapper.eq("phone", ordersPageQueryDTO.getPhone());
        }
        if(ordersPageQueryDTO.getStatus()!=null) {
            queryWrapper.eq("status", ordersPageQueryDTO.getStatus());
        }

        queryWrapper.orderByDesc("order_time");


       // queryWrapper.orderByAsc("number","phone","status");

      //  List<Orders> ordersList = orderMapper.getByUserId(ThreadLocalUtil.getCurrentId());
       // List<OrderDetail> orderDetailList = null;
        List<Orders> ordersList = orderMapper.getAll(queryWrapper);
        queryWrapper.orderByDesc();

        List<OrderVO> orderVOList =  BeanUtil.copyProperties(ordersList,ordersList.getClass());
        Page<OrderVO> page = (Page<OrderVO>)  orderVOList;


        for (Orders orders : ordersList) {
           OrderVO ordervo =BeanUtil.copyProperties(orders,OrderVO.class);
           orderVOList.add(ordervo);
//           ordervo.builder()
//                   .id(orders.getId())
//                   .number(orders.getNumber())
//                   .status(orders.getStatus())
//                   .userId(orders.getUserId())
//                   .addressBookId(orders.getAddressBookId())
//                   .orderTime(orders.getOrderTime())
//                   .checkoutTime(orders.getCheckoutTime())
//                   .payMethod(orders.getPayMethod())
//                   .amount(orders.getAmount())
//                   .remark(orders.getRemark())
//                   .userName(orders.getUserName())
//                   .phone(orders.getPhone())
//                   .address(orders.getAddress())
//                   .consignee(orders.getConsignee())
//                   .cancelReason(orders.getCancelReason())
//                   .rejectionReason(orders.getRejectionReason())
//                   .cancelTime(orders.getCancelTime())
//                   .estimatedDeliveryTime(orders.getEstimatedDeliveryTime())
//                   .deliveryStatus(orders.getDeliveryStatus())
//                   .

        }
        // 4.遍历voList
        for (OrderVO orderVO : orderVOList) {
            // 4-1 根据订单id查询明细列表
            List<OrderDetail> detailList = orderDetailMapper.selectByOrderId(orderVO.getId());
            // 4-2 设置到vo
            orderVO.setOrderDetailList(detailList);
        }

//        for (Orders orders : ordersList) {
//            List<OrderDetail> orderDetailList1 = orderDetailMapper.selectByOrderId(orders.getId());
//            OrderVO orderVO = null;
//            orderVO.setOrderDetailList(orderDetailList1);
//            orderVO.setOrderDishes(orderDetailList.get(0).getName());//有问题
////            for (OrderDetail orderDetail : orderDetailList1) {
//               // OrderVO orderVO = null;
//                //new OrderVO();
//                BeanUtil.copyProperties(orderDetail, orderVO);
//                orderVOList.add(orderVO);
//            }
//        PageResult pageResult = new PageResult();
//        pageResult.setTotal(orderVOList.size());
//        pageResult.setRecords(orderVOList);
        return new PageResult(page.getTotal(),orderVOList);
    }

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

        //Orders orders = new Orders();
//        BeanUtil.copyProperties(ordersSubmitDTO, orders);
//        BeanUtil.copyProperties(addressBook, orders, "id", "creatTime");
//
//        orders.setNumber(RandomUtil.randomNumbers(8)); // 订单号
//        orders.setAmount(ordersSubmitDTO.getAmount().add(BigDecimal.valueOf(ordersSubmitDTO.getPackAmount())));
//        orders.setOrderTime(LocalDateTime.now());
//        orders.setStatus(Orders.PENDING_PAYMENT);
//        orders.setPayStatus(Orders.UN_PAID);
//        orders.setAddress(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());// 地址信息
        Orders orders = BeanUtil.copyProperties(ordersSubmitDTO, Orders.class);
        orders.setNumber(RandomUtil.randomNumbers(8)); // 订单号
        orders.setStatus(Orders.PENDING_PAYMENT); // 订单状态 1待付款
        orders.setUserId(ThreadLocalUtil.getCurrentId()); // 下单用户
        orders.setOrderTime(LocalDateTime.now());// 下单时间
        orders.setPayStatus(Orders.UN_PAID);// 支付状态 0未支付
        orders.setPhone(addressBook.getPhone()); // 手机号
        orders.setAddress(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());// 地址信息
        orders.setConsignee(addressBook.getConsignee()); // 收货人
        // 3.保存订单
        orderMapper.insert(orders);
        // 4.查询购物车列表，遍历
        //orders.setId((long) random.nextInt(1000000000));


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
//
//            OrderDetail orderDetail1 = orderDetail;
//            BeanUtil.copyProperties(shoppingCart, orderDetail1);
//            orderDetailMapper.insert(orderDetail1);
//            orderDetail2 = orderDetail1;
//            shoppingCartMapper.deleteById(shoppingCart);

            // 4-1 购物项转订单明细
            orderDetail = BeanUtil.copyProperties(shoppingCart, OrderDetail.class, "id");
            // 4-2 订单明细设置订单id
            orderDetail.setOrderId(orders.getId());
            // 4-3 保存订单明细
            orderDetailMapper.insert(orderDetail);
        }
        // 4-4 清空购物车
        shoppingCartService.cleanCart();


        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        orderSubmitVO.setOrderNumber(orders.getNumber());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        // BeanUtil.copyProperties(orderDetail2, orderSubmitVO);

        orderSubmitVO.setOrderAmount(orders.getAmount());

        return orderSubmitVO;
    }

    // 订单支付
    @Transactional
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {

        // 1.直接修改订单状态
        Orders ordersDB = orderMapper.selectByNumber(ordersPaymentDTO.getOrderNumber());
        log.info(String.valueOf(ordersDB));

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.updateById(orders);

        // 2. 返回一个空结果
        OrderPaymentVO vo = OrderPaymentVO.builder()
                .nonceStr("52640818796650160489223277005653")
                .paySign("iHYG8l90s5nIXMWgkmN6PX2+3e4mW4spWMOLnvdQZTePZiMy/CDiP3CfvsByp65PpnVcmG1Br1EY7f46xeToKOlmK2qe00IFBaXUtNH/6+k5Ij7fXRKNRQxQuODegkWSvX+fw2FKo8qKT1clJd5/T/Hkwu6cSDZGqHIaW3eqha14HRpsT5siHlwoHw04X5wVvnktAx4Koko/tsMtI/t/dkCDvIbCve1ut7/FVVtlgNJKMR6rzY0wiyroseSy3qjbw6BUL+HPnxlLqF2PNbk9jkimyxJrwzxk2NFxjHM87tybMBMTITCuIuH9hZCFFbJTFsG9BYsL2H7GcsaYmzIoig==")
                .timeStamp("1683009626")
                .signType("RSA")
                .packageStr("prepay_id=wx02144025953621ab23eaa2fc334f2a0000")
                .build();

        return vo;
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
