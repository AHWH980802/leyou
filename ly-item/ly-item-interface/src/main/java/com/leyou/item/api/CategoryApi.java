package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 15:27 2019/4/30
 */
public interface CategoryApi {
    /**
     * 根据id查询品牌
     * @param ids
     * @return
     */
    @GetMapping("category/list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 查询导航面包屑
     * @param cid
     * @return
     */
    @GetMapping("category/cids")
    Category queryCids(@RequestParam("cid") Long cid);
}
