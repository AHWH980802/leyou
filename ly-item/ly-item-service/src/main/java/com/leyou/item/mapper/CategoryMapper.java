package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 11:15 2019/4/16
 */
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {
}
