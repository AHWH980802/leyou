package com.leyou.page.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 20:48 2019/5/10
 */
@Service
@Slf4j
public class PageService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient CategoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;

    public Map<String, Object> loadModel(Long spuId) {
        Map<String,Object> model = new HashMap<>();
        //  查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //  查询skus
        List<Sku> skus = spu.getSkus();
        //  查询详情
        SpuDetail detail = spu.getSpuDetail();
        //  查询brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //  查询商品分类
        List<Category> categories = CategoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //  查询规格参数
        List<SpecGroup> specs = specificationClient.queryGroupByCid(spu.getCid3());

        model.put("spu",spu);
        model.put("skus",skus);
        model.put("detail",detail);
        model.put("brand",brand);
        model.put("categories",categories);
        model.put("specs",specs);
        return model;
    }

    public void caretHtml(Long spuId){
        //  上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        //  输出流
        File file = new File("C:\\StudyData\\tensquare\\nginx-1.13.12\\html", spuId + ".html");

        //  判断是否存在 存在先删除
        if(file.exists()){
            file.delete();
        }

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            //  生成HTML
            templateEngine.process("item", context, writer);
        }catch (Exception e){
            log.error("[静态页服务]生成静态页异常",e);
        }
    }

    public void deleteHtml(Long spuId) {
        File file = new File("C:\\StudyData\\tensquare\\nginx-1.13.12\\html", spuId + ".html");
        if(file.exists()){
            file.delete();
        }
    }
}
