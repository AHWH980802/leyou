package com.leyou.page.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 15:32 2019/4/30
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
