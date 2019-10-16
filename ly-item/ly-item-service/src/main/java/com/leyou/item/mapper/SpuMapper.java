package com.leyou.item.mapper;

import com.leyou.item.pojo.Spu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 18:25 2019/4/22
 */
public interface SpuMapper extends Mapper<Spu> {

    @Update("UPDATE tb_spu SET saleable = #{sableable} WHERE id = #{id}")
    void saleableGoods(@Param("sableable") Boolean saleable,@Param("id") Long id);
}
