package com.leyou.page.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 9:35 2019/4/30
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {


}
