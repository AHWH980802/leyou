package com.leyou.common.advice;

import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 19:31 2019/4/15
 */
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(LyException.class)
    @ResponseBody
    public Map<String,Object> handleException(LyException e) {
        ExceptionResult exceptionResult = new ExceptionResult(e.getExceptionEnum());
        Map<String,Object> map = new HashMap<>();
        map.put("status",exceptionResult.getStatus());
        map.put("message",exceptionResult.getMessage());
        map.put("timestamp",exceptionResult.getTimestamp());
        return map;
    }
}
