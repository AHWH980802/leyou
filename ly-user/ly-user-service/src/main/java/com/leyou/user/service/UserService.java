package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 19:59 2019/5/14
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CODE_PREFIX = "user:verify:phone:";


    public Boolean checkData(String data, int type) {
        User user = new User();
        //  判断数据类型
        if (type == 1) {
            //  type为1 判断用户名
            user.setUsername(data);
        } else if (type == 2) {
            // type为2 判断电话
            user.setPhone(data);
        } else {
            //  否则抛出异常
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        return userMapper.selectCount(user) == 0;
    }

    public void sendCode(String phone) {
        //  发送验证码
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", phone);

    }

    public void register(User user, String code) {
        //  从redis取出验证码
        String cacheCode = redisTemplate.opsForValue().get(CODE_PREFIX + user.getPhone());
        //  校验验证码
        if(!StringUtils.equals(code,cacheCode)){
            throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
        }

        //  生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //  对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));

        //  写入数据库
        user.setCreated(new Date());
        userMapper.insert(user);
    }

    public User queryUserByUserNameAndPassword(String username, String password) {
        //  查询用户
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        //   校验
        if(user == null){
            throw  new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        // 校验密码
        if (!StringUtils.equals(user.getPassword(), CodecUtils.md5Hex(password,user.getSalt()))) {
            throw  new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        // 用户名和密码正确
        return user;
    }
}

