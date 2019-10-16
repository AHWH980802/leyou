package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 15:29 2019/4/30
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
