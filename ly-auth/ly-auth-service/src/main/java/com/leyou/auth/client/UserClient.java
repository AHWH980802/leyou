package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 17:00 2019/5/15
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
