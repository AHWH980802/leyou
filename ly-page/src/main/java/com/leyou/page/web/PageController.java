package com.leyou.page.web;

import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 16:37 2019/5/10
 */
@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("/{id}.html")
    public String toItemPage(@PathVariable("id") Long spuId, Model model){
        //  查询模型数据
        Map<String,Object> attributes = pageService.loadModel(spuId);
        System.out.println("进来了");
        //  准备模型数据
        model.addAllAttributes(attributes);

        //  返回页面视图
        return "item";
    }

}
