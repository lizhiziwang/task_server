package com.zsh.task.handler;


import com.zsh.task.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 处理 Exception 异常
     *
     * @param httpServletRequest httpServletRequest
     * @param e                  异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest httpServletRequest, Exception e) {
        log.error("服务错误:", e.getMessage());
        return Result.failed("服务发生异常，请稍后再试");
    }
}
