package com.leyou.order.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.auth.entity.UserInfo;
import com.leyou.common.dto.CartDto;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.IdWorker;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.order.client.AddressClient;
import com.leyou.order.client.GoodsClient;
import com.leyou.order.dto.AddressDTO;
import com.leyou.order.dto.OrderDto;
import com.leyou.order.enums.OrderStatusEnum;
import com.leyou.order.interceptors.UserInterceptor;
import com.leyou.order.mapper.*;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.OrderStatus;
import com.leyou.user.pojo.Address;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 21:10 2019/5/17
 */
@Service
@Slf4j
public class OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:user:id:";

    @Autowired
    private AddressClient addressClient;

    @Autowired
    private SpecParamMapper paramMapper;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapp;

    @Autowired
    private OrderStatusMapper statusMapper;

    @Autowired
    private IdWorker idWorker;

    @Transactional
    public String createOrder(OrderDto orderDto) {
        //  1 新增订单
        Order order = new Order();
        //  1.1 订单编号 基本信息
        String orderId = idWorker.nextId() + "";
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDto.getPaymentType());
        //  1.2 用户信息
        UserInfo user = UserInterceptor.gerUser();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);
        //  1.3 收货人地址
        Address addressDTO = addressClient.findById(orderDto.getAddressId());
        order.setReceiver(addressDTO.getName());
        order.setReceiverAddress(addressDTO.getAddress());
        order.setReceiverCity(addressDTO.getCity());
        order.setReceiverDistrict(addressDTO.getDistrict());
        order.setReceiverMobile(addressDTO.getPhone());
        order.setReceiverState(addressDTO.getState());
        order.setReceiverZip("000000");
        //  1.4 金额
        //  把cartDTO转成一个map
        Map<Long, Integer> numMap = orderDto.getCarts().stream().collect(Collectors.toMap(CartDto::getSkuId, CartDto::getNum));
        //  获取sku的id
        Set<Long> ids = numMap.keySet();
        List<Sku> skus = goodsClient.selectSkuByIds(new ArrayList<Object>(ids));
        Long totalPay = 0L;

        //  准备orderDetail集合
        List<OrderDetail> details = new ArrayList<>();
        for (Sku sku : skus) {
            totalPay += (sku.getPrice() * numMap.get(sku.getId()));

            //  封装orderDetail
            OrderDetail detail = new OrderDetail();
            detail.setImage(StringUtils.substringBefore(sku.getImages(), ","));
            detail.setNum(numMap.get(sku.getId()));
            detail.setOrderId(orderId+"");
            detail.setOwnSpec(sku.getOwnSpec());
            detail.setPrice(sku.getPrice());
            detail.setSkuId(sku.getId()+"");
            detail.setTitle(sku.getTitle());
            details.add(detail);
        }
        order.setTotalPay(totalPay);
        //  实付金额：总金额+邮费-优惠金额
        order.setActualPay(totalPay + order.getPostFee() - 0);

        // 1.5 写入数据
        int count = orderMapper.insert(order);
        if (count != 1) {
            log.error("[创建订单] 创建订单失败，orderId：{}", orderId);
            throw new LyException(ExceptionEnum.CREATED_ORDER_ERROR);
        }
        //  2 新增订单详情
        count = orderDetailMapp.insertList(details);
        if (count != details.size()) {
            log.error("[创建订单] 创建订单失败，orderId：{}", orderId);
            throw new LyException(ExceptionEnum.CREATED_ORDER_ERROR);
        }
        //  3 新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreateTime(order.getCreateTime());
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.UN_PAY.value());
        count = statusMapper.insertSelective(orderStatus);
        if (count != 1) {
            log.error("[创建订单] 创建订单失败，orderId：{}", orderId);
            throw new LyException(ExceptionEnum.CREATED_ORDER_ERROR);
        }
        //  4 减库存
        List<CartDto> carts = orderDto.getCarts();
        for (CartDto cart : carts) {
            //  减库存
            int c = stockMapper.decreaseStock(cart.getSkuId(), cart.getNum());
            if (c != 1) {
                log.error("[库存不足] 购买失败");
                throw new LyException(ExceptionEnum.STOCK_NOT_ENOYGH);
            }
        }

        //  创建订单成功后 删除购物车数据
        List<Long> skuIdList = orderDto.getCarts().stream().map(CartDto::getSkuId).collect(Collectors.toList());
        for (Long id : skuIdList) {
            //  获取用户
            UserInfo usera = UserInterceptor.gerUser();
            //  key
            String key = KEY_PREFIX + usera.getId();

            //  删除
            redisTemplate.opsForHash().delete(key,id.toString());
        }

        return orderId;
    }

    public Order queryOrderById(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order == null) {
            //  不存在
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        //  查询订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(id+"");
        List<OrderDetail> details = orderDetailMapp.select(detail);
        if (CollectionUtils.isEmpty(details)) {
            throw new LyException(ExceptionEnum.ORDER_DETAIL_NOT_FOUND);
        }
        order.setOrderDetails(details);
        //  查询订单状态
        OrderStatus orderStatus = statusMapper.selectByPrimaryKey(id);
        if (orderStatus == null) {
            throw new LyException(ExceptionEnum.ORDER_STATUS_NOT_FOUND);
        }
        order.setOrderStatus(orderStatus);
        return order;
    }

    public List<Order> queryOrderList(Long userId, int status) {
        Order order = new Order();
        order.setUserId(userId);
        List<Order> orders = orderMapper.select(order);
        if (CollectionUtils.isEmpty(orders)) {
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        //  查询订单详情
        for (int i = 0; i < orders.size(); i++) {
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(orders.get(i).getOrderId());
            detail = orderDetailMapp.selectOne(detail);
            if (detail == null) {
                throw new LyException(ExceptionEnum.ORDER_DETAIL_NOT_FOUND);
            }

            Set<String> keySet = JsonUtils.parseMap(detail.getOwnSpec(), String.class, String.class).keySet();
            Map<String, String> parseMap = JsonUtils.parseMap(detail.getOwnSpec(), String.class, String.class);
            Map<String, String> map = new HashMap<>();
            for (String s : keySet) {
                SpecParam specParam = paramMapper.selectByPrimaryKey(s);
                map.put(specParam.getName(), parseMap.get(s));
            }
            detail.setOwnSpec(JsonUtils.serialize(map));

            orders.get(i).setOrderDetails(Arrays.asList(detail));
            //  查询订单状态
            OrderStatus orderStatus = statusMapper.selectByPrimaryKey(orders.get(i).getOrderId());
            orders.get(i).setOrderStatus(orderStatus);

        }
        if (status == 0) {
            return orders;
        } else {
            List<Order> orderList = new ArrayList<>();
            for (Order order1 : orders) {
                if (order1.getOrderStatus().getStatus() == status) {
                    orderList.add(order1);
                }
            }
            return orderList;
        }
    }

    public void updateOrderStatus(Long orderId, int status) {
        //  查询orderStatus对象
        OrderStatus orderStatus = statusMapper.selectByPrimaryKey(orderId);
        if (status == 1) {
            orderStatus.setPaymentTime(new Date());
            orderStatus.setStatus(status + 1);
        } else if (status == 2) {
            orderStatus.setConsignTime(new Date());
            orderStatus.setStatus(status + 1);
        } else if (status == 3) {
            orderStatus.setEndTime(new Date());
            orderStatus.setStatus(status + 2);
        } else if (status == 5) {
            orderStatus.setCommentTime(new Date());
            orderStatus.setStatus(status + 1);
        }
        statusMapper.updateByPrimaryKeySelective(orderStatus);
    }
}