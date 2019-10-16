package com.leyou.order.enums;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 10:04 2019/5/18
 */
public enum OrderStatusEnum {

    UN_PAY(1,"未付款"),
    PAYED(2,"以付款未发货"),
    DELIVERED(3,"已发货，未确认"),
    SUCCESS(4,"已确认，未评价"),
    CLOSED(5,"已关闭，交易失败"),
    RATED(6,"已评价")
    ;
    private int code;
    private String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int value(){
        return this.code;
    }
}
