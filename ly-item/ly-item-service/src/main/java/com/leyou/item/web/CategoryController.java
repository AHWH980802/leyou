package com.leyou.item.web;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 11:17 2019/4/16
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点ID查询商品分类
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid") Long pid){
        System.out.println("进来了");
        return ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
    }


    /**
     * 根据id查询品牌
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }

    /**
     * 查询导航面包屑
     * @param cid
     * @return
     */
    @GetMapping("cids")
    public ResponseEntity<Category> queryCids(@RequestParam("cid") Long cid){
        return ResponseEntity.ok(categoryService.queryCids(cid));
    }
}
