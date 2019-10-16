package com.leyou.user.api;

import com.leyou.user.pojo.UserDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 10:37 2019/5/21
 */
public interface UserDetailApi {

    /**
     * 添加用户详情
     *
     * @param user
     */
    @PostMapping("/userDetail")
    void insertUserDetail(@RequestBody UserDetail user);
}
