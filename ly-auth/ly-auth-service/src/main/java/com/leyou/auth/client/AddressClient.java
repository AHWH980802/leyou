package com.leyou.auth.client;

import com.leyou.user.api.AddressApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 11:52 2019/5/21
 */
@FeignClient("user-service")
public interface AddressClient extends AddressApi {

}
