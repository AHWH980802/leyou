package com.leyou.cart.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 9:19 2019/5/17
 */
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:user:id:";

    public void addCart(Cart cart) {
        //  获取用户
        UserInfo user = UserInterceptor.gerUser();
        //  key
        String key = KEY_PREFIX + user.getId();
        saveToRedis(cart, key);


    }

    private void saveToRedis(Cart cart, String key) {
        //  HashKey
        String hashKey = cart.getSkuId().toString();
        //  记录num
        Integer num = cart.getNum();
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        //  判断当前商品是否存在
        if (operations.hasKey(hashKey)) {
            //  存在，增加
            String json = operations.get(hashKey).toString();
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum() + num);
        }
        //  写回redis
        operations.put(hashKey, JsonUtils.serialize(cart));
    }

    public List<Cart> queryCarts() {
        //  获取用户
        UserInfo user = UserInterceptor.gerUser();
        //  key
        String key = KEY_PREFIX + user.getId();
        if (!redisTemplate.hasKey(key)) {
            //  key不存在，返回404
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        //  获取登陆用户的所有购物车
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        return operations.values().stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
    }

    public void updateCartNum(Long id, Integer num) {
        //  获取用户
        UserInfo user = UserInterceptor.gerUser();
        //  key
        String key = KEY_PREFIX + user.getId();

        //  hashKey
        String hashKey = id.toString();

        //  获取操作
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);

        //  判断是否存在
        if(!operations.hasKey(hashKey)){
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }

        //  查询
        Cart cart = JsonUtils.parse(operations.get(hashKey).toString(), Cart.class);
        cart.setNum(num);
        operations.put(hashKey, JsonUtils.serialize(cart));

    }

    public void deleteCart(Long id) {
        //  获取用户
        UserInfo user = UserInterceptor.gerUser();
        //  key
        String key = KEY_PREFIX + user.getId();

        //  删除
        redisTemplate.opsForHash().delete(key,id.toString());
    }

    public void addCarts(List<Cart> cart) {
        //  获取用户
        UserInfo user = UserInterceptor.gerUser();
        //  key
        String key = KEY_PREFIX + user.getId();

        //  循环调用方法存入redis
        cart.forEach(c -> saveToRedis(c,key));
    }


}
