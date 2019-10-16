package com.leyou.auth.web;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.user.pojo.Address;
import com.leyou.user.pojo.User;
import com.leyou.user.pojo.UserDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 16:40 2019/5/15
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties prop;

    /**
     * 登陆授权
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        //  登陆
        String token = authService.login(username, password);
        //  写入cookie
        CookieUtils.newBuilder(response).httpOnly().request(request).build("LY_TOKEN", token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue("LY_TOKEN") String token) {

        try {
            //  解析token
            UserInfo info = JwtUtils.getInfoFromToken(token, prop.getPublicKey());

            //  刷新token，重新生成token
            String newToken = JwtUtils.generateToken(info, prop.getPrivateKey(), prop.getExpire());

            //  写入cookie
            CookieUtils.newBuilder(response).httpOnly().request(request).build("LY_TOKEN", newToken);


            //  已登录 返回用户信息
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            //  token过期 或者 token被篡改
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }

    }

    /**
     * 查询所有地址
     * @param userId
     * @return
     */
    @GetMapping("address")
    public ResponseEntity<List<Address>> queryAddressByUserId(@RequestParam("userId") Long userId){
        return ResponseEntity.ok(authService.queryAddressByUserId(userId));
    }

    /**
     * 新增收货地址
     */
    @PostMapping("address")
    public ResponseEntity<Void> insertAddress(Address address,@RequestParam("userId") Long userId){
        authService.insertAddress(address,userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改收货地址
     */
    @PutMapping("address")
    public ResponseEntity<Void> updateAddress(Address address){
        authService.updateAddress(address);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改默认收货地址
     */
    @PutMapping("address/modify/{userId}/{addressId}")
    public ResponseEntity<Void> modifyAddress(@PathVariable("userId") Long userId,@PathVariable("addressId") Long addressId){
        authService.modifyAddress(userId,addressId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除收货地址
     */
    @DeleteMapping("address")
    public ResponseEntity<Void> delAddress(@RequestParam("addressId") Long addressId){
        authService.delAddress(addressId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
