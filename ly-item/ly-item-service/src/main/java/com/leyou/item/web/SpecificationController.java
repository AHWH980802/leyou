package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 8:56 2019/4/22
 */
@RestController
@RequestMapping("spec")
@Slf4j
public class SpecificationController {


    @Autowired
    private SpecificationService specificationService;


    /**
     * 根据分类id查询规格组
     *
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryByGroup(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(specificationService.queryByGroup(cid));
    }

    /**
     * 新增分类
     *
     * @param specGroup
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> addGroup(@RequestBody SpecGroup specGroup) {
        specificationService.addGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改分组
     *
     * @param specGroup
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateGroup(@RequestBody SpecGroup specGroup) {
        specificationService.updateGroup(specGroup);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 根据id删除分组
     *
     * @param id
     * @return
     */
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        specificationService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 根据id查询分组参数
     *
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamsList(@RequestParam(value = "gid",required = false) Long gid, @RequestParam(value = "cid",required = false) Long cid,@RequestParam(value = "searching",required = false) Boolean searching) {
        return ResponseEntity.ok(specificationService.queryParamsList(gid,cid,searching));
    }


    /**
     * 新增参数
     *
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> addParam(@RequestBody SpecParam specParam) {
        specificationService.addParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改参数
     *
     * @param specParam
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateParam(@RequestBody SpecParam specParam) {
        specificationService.updateParam(specParam);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * @param id
     * @return
     */
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteParam(@PathVariable Long id) {
        specificationService.deleteParam(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 根据分类查询规格组及组内参数
     * @param cid
     * @return
     */
    @GetMapping("/group")
    public ResponseEntity<List<SpecGroup>> queryListByCid(@RequestParam("cid") Long cid){
        return ResponseEntity.ok(specificationService.queryListByCid(cid));
    }
}
