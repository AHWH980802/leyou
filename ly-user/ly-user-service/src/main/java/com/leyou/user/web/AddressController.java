package com.leyou.user.web;

import com.leyou.user.pojo.Address;
import com.leyou.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 11:47 2019/5/21
 */
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> queryAddressByUserId(@RequestParam("userId") Long userId){
        return ResponseEntity.ok(addressService.queryAddressByUserId(userId));
    }

    /**
     * 新增收货地址
     */
    @PostMapping
    public ResponseEntity<Void> insertAddress(@RequestBody Address address,@RequestParam("userId") Long userId){
        addressService.insertAddress(address,userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 修改收货地址
     */
    @PutMapping
    public ResponseEntity<Void> updateAddress(@RequestBody Address address){
        addressService.updateAddress(address);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改默认收货地址
     */
    @PutMapping("modify/{userId}/{addressId}")
    public ResponseEntity<Void> modifyAddress(@PathVariable("userId") Long userId,@PathVariable("addressId") Long addressId){
        addressService.modifyAddress(userId,addressId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除收货地址
     */
    @DeleteMapping
    public ResponseEntity<Void> delAddress(@RequestParam("addressId") Long addressId){
        addressService.delAddress(addressId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/findById")
    public ResponseEntity<Address> findById(@RequestParam("addressId") Long addressId){
        return ResponseEntity.ok(addressService.findById(addressId));
    }
}
