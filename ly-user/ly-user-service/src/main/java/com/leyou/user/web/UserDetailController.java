package com.leyou.user.web;


import com.leyou.user.pojo.UserDetail;
import com.leyou.user.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 10:33 2019/5/21
 */
@RestController
@RequestMapping("userDetail")
public class UserDetailController {

    @Autowired
    private UserDetailService detailService;

    @PostMapping
    public ResponseEntity<Void> insertUserDetail(@RequestBody UserDetail user){
        detailService.insertUserDetail(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
