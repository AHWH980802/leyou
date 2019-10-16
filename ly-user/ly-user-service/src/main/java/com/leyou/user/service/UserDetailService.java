package com.leyou.user.service;

import com.leyou.user.mapper.UserDetailMapper;
import com.leyou.user.pojo.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 10:32 2019/5/21
 */
@Service
public class UserDetailService {

    @Autowired
    private UserDetailMapper detailMapper;

    public void insertUserDetail(UserDetail user) {
        detailMapper.insert(user);
    }
}
