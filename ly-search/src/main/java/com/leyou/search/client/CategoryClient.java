package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import com.leyou.item.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 9:35 2019/4/30
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {


}
