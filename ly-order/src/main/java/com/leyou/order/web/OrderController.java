package com.leyou.order.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.order.config.JwtProperties;
import com.leyou.order.dto.OrderDto;
import com.leyou.order.pojo.Order;
import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 21:06 2019/5/17
 */
@RestController
@RequestMapping("/order")
@EnableConfigurationProperties(JwtProperties.class)
public class OrderController {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private OrderService orderService;


    /**
     * 创建订单
     *
     * @param orderDto
     * @return
     */
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.createOrder(orderDto));

    }

    /**
     * 根据id查询订单
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") String id) {
        return ResponseEntity.ok(orderService.queryOrderById(id));
    }

    /**
     * 查询当前登录用户的全部订单信息
     * @param token
     * @param status
     * @return
     */
    @GetMapping("list")
    public List<Order> queryOrderList(@CookieValue("LY_TOKEN") String token,@RequestParam(value = "status",required = false) int status) {
        //  获取当前用户信息
        //  解析token
        UserInfo info;
        try {
            info = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
        } catch (Exception e) {
            //  token过期 或者 token被篡改
            throw new LyException(ExceptionEnum.CREATE_TOKEN_ERROR);
        }
        List<Order> orders = orderService.queryOrderList( info.getId(),status);
        return orders;
    }

    @PutMapping("/{orderId}/{status}")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable("orderId") Long orderId,@PathVariable("status") int status){
        System.out.println("orderId = " + orderId);
        System.out.println("status = " + status);
        orderService.updateOrderStatus(orderId,status);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
