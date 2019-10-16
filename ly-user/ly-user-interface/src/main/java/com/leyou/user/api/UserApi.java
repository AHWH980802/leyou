package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 16:57 2019/5/15
 */
public interface UserApi {

    /**
     * 根据用户名和密码查询用户
     *
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    User queryUserByUserNameAndPassword(@RequestParam("username") String username, @RequestParam("password") String password);

}
