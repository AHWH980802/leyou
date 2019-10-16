package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 19:48 2019/4/15
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    BRAND_NOT_FOUND(400, "品牌不存在"),
    CATEGORY_NOT_FOUND(404, "商品分类不存在"),
    ORDER_NOT_FOUND(404, "订单不存在"),
    ORDER_DETAIL_NOT_FOUND(404, "订单详情不存在"),
    ORDER_STATUS_NOT_FOUND(404, "订单状态不存在"),
    CART_NOT_FOUND(404, "购物车未找到"),
    GOODS_NOT_FOUND(404, "商品不存在"),
    ADDRESS_NOT_FOUND(404, "暂无地址"),
    GOODS_SKU_NOT_FOUND(404, "商品SKU不存在"),
    GOODS_STOCK_NOT_FOUND(404, "商品库存不存在"),
    GOODS_DETAIL_NOT_FOUND(404, "商品详情不存在"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    GROUP_SAVE_ERROR(500,"新增分组失败"),
    CATEGORY_BRAND_SAVE_ERROR(500, "新增品牌失败"),
    UPLOAD_FILE_ERROR(500, "上传文件失败"),
    INVALID_FILE_TYPE(400, "无效的文件类型"),
    SPEC_GROUP_NOT_FOUND(404, "商品规格组不存在"),
    SPEC_PARAM_NOT_FOUND(404, "商品规格参数不存在"),
    GOODS_ID_CANNOT_BE_NULL(400, "商品id不能为空"),
    GOODS_SAVE_ERROR(500, "商品保存失败"),
    GOODS_UPDATE_ERROR(500, "商品修改失败"),
    SPU_DETAIL_UPDATE_ERROR(500, "spuDetail修改失败"),
    INVALID_USER_DATA_TYPE(400,"无效的数据类型"),
    INVALID_VERIFY_CODE(400,"无效的验证码"),
    INVALID_USERNAME_PASSWORD(400,"无效的用户名密码"),
    CREATE_TOKEN_ERROR(500,"用户凭证生成失败"),
    UNAUTHORIZED(403,"无权访问"),
    CREATED_ORDER_ERROR(500,"创建订单失败"),
    STOCK_NOT_ENOYGH(500,"库存不足"),
    ;


    private int code;
    private String msg;


}
