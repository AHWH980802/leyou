package com.leyou.item.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 20:50 2019/5/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long skuId;
    private Integer num;
}
