package com.xmxe.comonent;

import com.xmxe.entity.ResultInfo;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常定义
 */
@RestControllerAdvice
public class CustomGlobalException {

    @ExceptionHandler(Exception.class)
    public ResultInfo<String> excetionHand(Exception e){
        return new ResultInfo<>(110,"出错了",e.getMessage());
    }
}
