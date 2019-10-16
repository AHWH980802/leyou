package com.leyou.item.web;

import com.leyou.common.dto.CartDto;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 18:27 2019/4/22
 */
@RestController
@Slf4j
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

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
    public ResponseEntity<PageResult<Spu>> querySpuByPage(@RequestParam(value = "key", required = false) String key,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "saleable", required = false) Boolean saleable,
                                                          @RequestParam(value = "rows", defaultValue = "5") Integer rows) {
        return ResponseEntity.ok(goodsService.querySpuByPage(key, page, saleable, rows));
    }


    /**
     * 商品新增
     *
     * @param spu
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 商品修改
     *
     * @param spu
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu) {
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 根据id修改商品上架状态
     *
     * @param saleable
     * @param id
     * @return
     */
    @PutMapping("/sku/saleable")
    public ResponseEntity<Void> saleableGoods(@RequestParam("saleable") Boolean saleable, @RequestParam("id") Long id) {
        goodsService.saleableGoods(saleable, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/spu/deleteGoods")
    public ResponseEntity<Void> deleteGoods(@RequestParam("id") Long id) {
        goodsService.deleteGoods(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 修改前查询spuDetail信息
     *
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> selectSpuDetailForUpdate(@PathVariable("id") Long id) {
        return ResponseEntity.ok(goodsService.selectSpuDetailForUpdate(id));
    }

    /**
     * 修改前查询Sku信息
     *
     * @param id
     * @return
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> selectSkuForUpdate(@RequestParam("id") Long id) {
        return ResponseEntity.ok(goodsService.selectSkuForUpdate(id));
    }

    /**
     * 修改前查询Sku的id集合查询sku信息
     *
     * @param ids
     * @return
     */
    @GetMapping("/sku/list/ids")
    public ResponseEntity<List<Sku>> selectSkuByIds(@RequestParam("ids") List<Object> ids) {
        return ResponseEntity.ok(goodsService.selectSkuByIds(ids));
    }


    /**
     * 根据Spu的id查询Spu
     *
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(goodsService.querySpuById(id));
    }

    /**
     * 减库存
     * @param carts
     * @return
     */
    @PostMapping("stock/decrease")
    public ResponseEntity<Void> decreaseStock(@RequestBody List<CartDto> carts) {
        goodsService.decreaseStock(carts);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
