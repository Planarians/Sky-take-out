package com.sky.exception;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 全局异常处理器
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    // 预期异常
    @ExceptionHandler(BusinessException.class)
    public Result handlerBusinessException(BusinessException e){
        // 记录异常日志
        log.error("出现业务异常：{}",e);
        // 返回提示信息
        return Result.error(e.getCode(),e.getMessage());
    }


    // 非预期异常
    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e){
        // 记录异常日志
        log.error("出现未知异常：{}",e);
        // 返回提示信息
        return Result.error(500,"未知异常，请稍后重试");
    }
}
