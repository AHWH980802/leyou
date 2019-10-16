package com.leyou.user.api;

import com.leyou.user.pojo.Address;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 11:53 2019/5/21
 */
public interface AddressApi {

    @GetMapping("address")
    List<Address> queryAddressByUserId(@RequestParam("userId") Long userId);

    @PostMapping("address")
    void insertAddress(@RequestBody Address address, @RequestParam("userId") Long userId);

    @PutMapping("address")
    void updateAddress(@RequestBody Address address);

    /**
     * 修改默认收货地址
     */
    @PutMapping("address/modify/{userId}/{addressId}")
    void modifyAddress(@PathVariable("userId") Long userId, @PathVariable("addressId") Long addressId);

    /**
     * 删除收货地址
     */
    @DeleteMapping("address")
    void delAddress(@RequestParam("addressId") Long addressId);

    @GetMapping("/address/findById")
    Address findById(@RequestParam("addressId") Long addressId);
}
