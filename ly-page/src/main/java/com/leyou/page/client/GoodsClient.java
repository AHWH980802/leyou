package com.leyou.page.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 9:43 2019/4/30
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {


}
