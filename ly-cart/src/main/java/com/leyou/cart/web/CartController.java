package com.leyou.cart.web;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 9:11 2019/5/17
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 新增购物车
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 同步购物车
     * @param cart
     * @return
     */
    @PostMapping("/listCarts")
    public ResponseEntity<Void> addCarts(@RequestBody List<Cart> cart){
        cartService.addCarts(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询购物车
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Cart>> queryCarts(){
        return ResponseEntity.ok(cartService.queryCarts());
    }

    /**
     * 修改商品数量
     * @param id/cart
     * @param num
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateCartNum(@RequestParam("id") Long id,@RequestParam("num") Integer num){
        cartService.updateCartNum(id,num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 删除购物车
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable("id") Long id){
        cartService.deleteCart(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
