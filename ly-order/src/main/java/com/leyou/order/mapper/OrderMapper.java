package com.leyou.order.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.order.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 21:11 2019/5/17
 */
public interface OrderMapper extends BaseMapper<Order> {

    @Select("SELECT tb_order.* FROM tb_order , tb_order_status WHERE tb_order.`order_id` = tb_order_status.`order_id` AND tb_order_status.status = #{status} AND tb_order.`user_id` = #{userId}")
    List<Order> selectOrders(@Param("status") Integer status,@Param("userId") Long userId);
}
