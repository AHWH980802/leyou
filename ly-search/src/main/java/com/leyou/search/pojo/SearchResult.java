package com.leyou.search.pojo;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 8:38 2019/5/7
 */

@Data
public class SearchResult extends PageResult<Goods> {

    //分类待选项
    private List<Category> categories;

    //  品牌待选项
    private List<Brand> brands;

    //  规格参数 key及待选项
    private List<Map<String,Object>> specs;

    private List<String> categoriesNames;

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs, List<String> categoriesName) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
        this.categoriesNames = categoriesName;
    }
}
