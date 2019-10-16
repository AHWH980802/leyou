package com.leyou.item.api;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 9:50 2019/4/30
 */
public interface GoodsApi {
    /**
     * 修改前查询spuDetail信息
     *
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail selectSpuDetailForUpdate(@PathVariable("id") Long id);

    /**
     * 修改前查询Sku信息
     *
     * @param id
     * @return
     */
    @GetMapping("/sku/list")
    List<Sku> selectSkuForUpdate(@RequestParam("id") Long id);

    /**
     * 分页查询spu
     *
     * @param key
     * @param page
     * @param saleable
     * @param rows
     * @return
     */
    @GetMapping("/spu/page")
    PageResult<Spu> querySpuByPage(@RequestParam(value = "key", required = false) String key,
                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "saleable", required = false) Boolean saleable,
                                   @RequestParam(value = "rows", defaultValue = "5") Integer rows);


    /**
     * 根据Spu的id查询Spu
     *
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);

    /**
     * 修改前查询Sku的id集合查询sku信息
     *
     * @param ids
     * @return
     */
    @GetMapping("/sku/list/ids")
    List<Sku> selectSkuByIds(@RequestParam("ids") List<Object> ids);

    /**
     * 减库存
     *
     * @param carts
     * @return
     */
    @PostMapping("stock/decrease")
    void decreaseStock(@RequestBody List<CartDto> carts);

}
