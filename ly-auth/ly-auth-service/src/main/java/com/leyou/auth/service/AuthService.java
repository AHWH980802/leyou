package com.leyou.auth.service;

import com.leyou.auth.client.AddressClient;
import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.user.pojo.Address;
import com.leyou.user.pojo.User;
import com.leyou.user.pojo.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 16:41 2019/5/15
 */
@Service
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private AddressClient addressClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties prop;

    public String login(String username, String password) {
        try {
            //  校验用户名和密码
            User user = userClient.queryUserByUserNameAndPassword(username, password);

            //  判断
            if (user == null) {
                throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
            }

            //  生成token

            return JwtUtils.generateToken(new UserInfo(user.getId(), username), prop.getPrivateKey(), prop.getExpire());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("[授权中心] 用户名密码有误，用户名称{}", username, e);
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }

    }



    public List<Address> queryAddressByUserId(Long userId) {
        return addressClient.queryAddressByUserId(userId);
    }

    public void insertAddress(Address address, Long userId) {
        address.setUserId(null);
        addressClient.insertAddress(address, userId);
    }

    public void updateAddress(Address address) {
        addressClient.updateAddress(address);
    }

    public void modifyAddress(Long userId, Long addressId) {
        addressClient.modifyAddress(userId,addressId);
    }

    public void delAddress(Long addressId) {
        addressClient.delAddress(addressId);
    }
}
