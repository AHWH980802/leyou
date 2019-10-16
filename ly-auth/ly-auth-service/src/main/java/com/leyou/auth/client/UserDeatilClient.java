package com.leyou.auth.client;

import com.leyou.user.api.UserDetailApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 10:39 2019/5/21
 */
@FeignClient("user-service")
public interface UserDeatilClient extends UserDetailApi {
}
