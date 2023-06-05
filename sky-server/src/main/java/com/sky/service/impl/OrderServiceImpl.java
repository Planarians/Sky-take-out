package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.ThreadLocalUtil;
import com.sky.dto.*;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
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

    @Value("${sky.shop.address}")
    private String shopAddress;

    @Value("${sky.baidu.ak}")
    private String ak;

    @Autowired
    private WebSocketServer webSocketServer;


    @Override
    public PageResult getPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        //Orders orders = orderMapper.selectById(ordersPageQueryDTO.get)
        ordersPageQueryDTO.setUserId(ThreadLocalUtil.getCurrentId());
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        //  List<OrderVO> voList = orderMapper.getList(ordersPageQueryDTO);

        QueryWrapper<OrderVO> queryWrapper = new QueryWrapper<>();

        if (ordersPageQueryDTO.getNumber() != null) {
            queryWrapper.eq("number", ordersPageQueryDTO.getNumber());
        }
        if (ordersPageQueryDTO.getPhone() != null) {
            queryWrapper.eq("phone", ordersPageQueryDTO.getPhone());
        }
        if (ordersPageQueryDTO.getStatus() != null) {
            queryWrapper.eq("status", ordersPageQueryDTO.getStatus());
        }

        queryWrapper.orderByDesc("order_time");


        // queryWrapper.orderByAsc("number","phone","status");

        //  List<Orders> ordersList = orderMapper.getByUserId(ThreadLocalUtil.getCurrentId());
        // List<OrderDetail> orderDetailList = null;
        List<Orders> ordersList = orderMapper.getAll(queryWrapper);
        queryWrapper.orderByDesc();

        List<OrderVO> orderVOList = BeanUtil.copyProperties(ordersList, ordersList.getClass());
        Page<OrderVO> page = (Page<OrderVO>) orderVOList;


        for (Orders orders : ordersList) {
            OrderVO ordervo = BeanUtil.copyProperties(orders, OrderVO.class);
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
        return new PageResult(page.getTotal(), orderVOList);
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

        checkOutOfRange(addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail());
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
//        OrderPaymentVO vo = OrderPaymentVO.builder()
//                .nonceStr("52640818796650160489223277005653")
//                .paySign("iHYG8l90s5nIXMWgkmN6PX2+3e4mW4spWMOLnvdQZTePZiMy/CDiP3CfvsByp65PpnVcmG1Br1EY7f46xeToKOlmK2qe00IFBaXUtNH/6+k5Ij7fXRKNRQxQuODegkWSvX+fw2FKo8qKT1clJd5/T/Hkwu6cSDZGqHIaW3eqha14HRpsT5siHlwoHw04X5wVvnktAx4Koko/tsMtI/t/dkCDvIbCve1ut7/FVVtlgNJKMR6rzY0wiyroseSy3qjbw6BUL+HPnxlLqF2PNbk9jkimyxJrwzxk2NFxjHM87tybMBMTITCuIuH9hZCFFbJTFsG9BYsL2H7GcsaYmzIoig==")
//                .timeStamp("1683009626")
//                .signType("RSA")
//                .packageStr("prepay_id=wx02144025953621ab23eaa2fc334f2a0000")
//                .build();
//
//        return vo;


        //--------- webSocket 发送消息给客户端 ------------
        Map map = new HashMap();
        map.put("type", 1);//消息类型，1表示来单提醒
        map.put("orderId", orders.getId());
        map.put("content", "订单号：" + ordersPaymentDTO.getOrderNumber());
        //通过WebSocket实现来单提醒，向客户端浏览器推送消息
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
        //--------- webSocket 发送消息给客户端 ------------


        // 2. 返回一个空结果
        return new OrderPaymentVO();
    }

    @Override
    public OrderVO getOrderDetail(Long id) {
        Orders orders = orderMapper.getById(id);
        OrderVO orderVO = BeanUtil.copyProperties(orders, OrderVO.class);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectByOrderId(orders.getId());
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    @Transactional
    @Override
    public void cancelOrder(Long id) {


        Orders orders = orderMapper.getById(id);
        if (orders.getStatus() > 2) {
            throw new BusinessException(400, "无法取消不是待付款或待接单状态");
        } else if (orders.getStatus() == 2) {
            orders.setStatus(6);
            orders.setPayStatus(2);
        } else {
            orders.setStatus(6);
            orders.setCancelReason("用户取消订单");
            orders.setCancelTime(LocalDateTime.now());
        }


        orderMapper.updateById(orders);
    }

    @Transactional
    @Override
    public void repetition(Long id) {
        Orders orders = orderMapper.getById(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectByOrderId(id);
        // 清空购物车
        shoppingCartService.cleanCart();
        for (OrderDetail orderDetail : orderDetailList) {
            ShoppingCart shoppingCart = BeanUtil.copyProperties(orderDetail, ShoppingCart.class);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setId(null);
            shoppingCart.setUserId(ThreadLocalUtil.getCurrentId());
            shoppingCartMapper.insert(shoppingCart);
        }


    }

    @Override
    public PageResult getAdmainPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        ordersPageQueryDTO.setUserId(ThreadLocalUtil.getCurrentId());
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        //  List<OrderVO> voList = orderMapper.getList(ordersPageQueryDTO);

        QueryWrapper<OrderVO> queryWrapper = new QueryWrapper<>();

        if (ordersPageQueryDTO.getNumber() != null) {
            queryWrapper.eq("number", ordersPageQueryDTO.getNumber());
        }
        if (ordersPageQueryDTO.getPhone() != null) {
            queryWrapper.eq("phone", ordersPageQueryDTO.getPhone());
        }
        if (ordersPageQueryDTO.getStatus() != null) {
            queryWrapper.eq("status", ordersPageQueryDTO.getStatus());
        }
        if (ordersPageQueryDTO.getBeginTime() != null) {
            queryWrapper.ge("begin_time", ordersPageQueryDTO.getBeginTime());
        }
        if (ordersPageQueryDTO.getEndTime() != null) {
            queryWrapper.le("end_time", ordersPageQueryDTO.getEndTime());
        }

        queryWrapper.orderByDesc("order_time");
        List<Orders> ordersList = orderMapper.getAll(queryWrapper);
        queryWrapper.orderByDesc();

        List<OrderVO> orderVOList = BeanUtil.copyProperties(ordersList, ordersList.getClass());
        Page<OrderVO> page = (Page<OrderVO>) orderVOList;


        for (Orders orders : ordersList) {
            OrderVO ordervo = BeanUtil.copyProperties(orders, OrderVO.class);
            orderVOList.add(ordervo);
        }

        for (OrderVO orderVO : orderVOList) {
            // 4-1 根据订单id查询明细列表
            List<OrderDetail> detailList = orderDetailMapper.selectByOrderId(orderVO.getId());
            // 4-2 设置到vo
            orderVO.setOrderDetailList(detailList);
            for (OrderDetail orderDetail : detailList) {
                orderVO.setOrderDishes(orderVO.getOrderDishes() + orderDetail.getName() + "*" + orderDetail.getNumber() + ";");
            }
        }


        return new PageResult(page.getTotal(), orderVOList);
    }


    @Override
    public OrderStatisticsVO getStatistics() {

        OrderStatisticsVO orderStatisticsVO = OrderStatisticsVO.builder()
                .confirmed(orderMapper.getStatusNumber(Orders.CONFIRMED))
                .deliveryInProgress(orderMapper.getStatusNumber(Orders.DELIVERY_IN_PROGRESS))
                .toBeConfirmed(orderMapper.getStatusNumber(Orders.TO_BE_CONFIRMED)).build();


//        // System.out.println("rdasdasdsadsdsad"+orderMapper.getStatusNumber(Orders.TO_BE_CONFIRMED));
//        orderStatisticsVO.setConfirmed(orderMapper.getStatusNumber(Orders.CONFIRMED));
//
//        //!= null ? orderMapper.getStatusNumber(2) : 0);
//        orderStatisticsVO.setDeliveryInProgress(orderMapper.getStatusNumber(Orders.DELIVERY_IN_PROGRESS));
//
//        //!= null ? orderMapper.getStatusNumber(4) : 0);
//        orderStatisticsVO.setToBeConfirmed(orderMapper.getStatusNumber(Orders.TO_BE_CONFIRMED));
//        //!= null ? orderMapper.getStatusNumber(1) : 0);//?


        return orderStatisticsVO;

//
//            // 根据状态，分别查询出待接单、待派送、派送中的订单数量
//            Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
//            Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
//            Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);
//
//            // 将查询出的数据封装到orderStatisticsVO中响应
//            OrderStatisticsVO statisticsVO = OrderStatisticsVO.builder()
//                    .toBeConfirmed(toBeConfirmed)
//                    .confirmed(confirmed)
//                    .deliveryInProgress(deliveryInProgress)
//                    .build();
//            return statisticsVO;

    }

    @Transactional
    @Override
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = orderMapper.getById(ordersConfirmDTO.getId());
        orders.setStatus(Orders.CONFIRMED);
        orderMapper.updateById(orders);


    }

    @Override
    public void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO) {

        Orders orders = orderMapper.getById((ordersRejectionDTO.getId()));
        if (orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            orders.setStatus(Orders.CANCELLED);
            //orders.setPayStatus(2);
            orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
            orders.setCancelTime(LocalDateTime.now());
            orderMapper.updateById(orders);
        } else {
            throw new BusinessException(400, "订单状态错误");

        }
    }

    @Override
    public void cancelOrderAdmin(OrdersCancelDTO ordersCancelDTO) {
        Orders orders = orderMapper.getById(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(orders);

    }

    @Override
    public void deliveryOrder(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        } else {
            throw new BusinessException(400, "订单状态错误");
        }

    }

    @Override
    public void completeOrder(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            orders.setStatus(Orders.COMPLETED);
            orderMapper.updateById(orders);
        } else{
            throw new BusinessException(400, "订单状态错误");

        }

    }




    /**
     * 检查客户的收货地址是否超出配送范围
     * @param address
     */
    private void checkOutOfRange(String address) {
        Map map = new HashMap();
        map.put("address",shopAddress);
        map.put("output","json");
        map.put("ak",ak);

        //获取店铺的经纬度坐标
        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(shopCoordinate);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("店铺地址解析失败");
        }

        //数据解析
        com.alibaba.fastjson.JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        String lat = location.getString("lat");
        String lng = location.getString("lng");
        //店铺经纬度坐标
        String shopLngLat = lat + "," + lng;

        map.put("address",address);
        //获取用户收货地址的经纬度坐标
        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        jsonObject = JSON.parseObject(userCoordinate);
        if(!jsonObject.getString("status").equals("0")){
            throw new BusinessException("收货地址解析失败");
        }

        //数据解析
        location = jsonObject.getJSONObject("result").getJSONObject("location");
        lat = location.getString("lat");
        lng = location.getString("lng");
        //用户收货地址经纬度坐标
        String userLngLat = lat + "," + lng;

        map.put("origin",shopLngLat);
        map.put("destination",userLngLat);


        //路线规划
        String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving", map);

        jsonObject = JSON.parseObject(json);
        if(!jsonObject.getString("status").equals("0")){
            throw new BusinessException("配送路线规划失败");
        }

        //数据解析
        com.alibaba.fastjson.JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");

        if(distance > 5000){
            //配送距离超过5000米
            throw new BusinessException("超出配送范围");
        }
    }


    // 用户催单
    @Override
    public void reminder(Long id) {
        // 查询订单是否存在
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new BusinessException("订单不存在");
        }

        //基于WebSocket实现催单
        Map map = new HashMap();
        map.put("type", 2);//2代表用户催单
        map.put("orderId", id);
        map.put("content", "订单号：" + orders.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
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
