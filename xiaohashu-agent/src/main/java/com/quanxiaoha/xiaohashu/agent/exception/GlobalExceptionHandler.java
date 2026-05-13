package com.quanxiaoha.xiaohashu.agent.exception;

import com.quanxiaoha.framework.common.exception.BizException;
import com.quanxiaoha.framework.common.response.Response;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, BizException.class})
    public Response<?> handleValidation(Exception e) {
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response<?> handleException(Exception e) {
        return Response.fail("Agent 服务异常：" + e.getMessage());
    }
}
