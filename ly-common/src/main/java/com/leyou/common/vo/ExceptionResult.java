package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnum;
import lombok.Data;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 20:00 2019/4/15
 */
@Data
public class ExceptionResult {

    private Integer status;
    private String message;
    private Long timestamp;


    public ExceptionResult(ExceptionEnum exceptionEnum) {
        this.message = exceptionEnum.getMsg();
        this.status = exceptionEnum.getCode();
        this.timestamp = System.currentTimeMillis();
    }
}
