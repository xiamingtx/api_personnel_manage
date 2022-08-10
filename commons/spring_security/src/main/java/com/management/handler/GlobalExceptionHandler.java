package com.management.handler;

import com.management.common.ResponseResult;
import com.management.exception.BizException;
import com.management.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 夏明
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BizException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Void> bizExceptionHandler(BizException e) {
        ResponseResult<Void> errorResponse = new ResponseResult<>();
        errorResponse.setCode(e.getCode());
        errorResponse.setMessage(e.getMessage());
        e.printStackTrace();
        log.error(e.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Void> exceptionHandler(Exception e) {
        ResponseResult<Void> errorResponse = new ResponseResult<>();
        errorResponse.setCode(ExceptionType.INNER_ERROR.getCode());
        errorResponse.setMessage(ExceptionType.INNER_ERROR.getMessage());
        e.printStackTrace();
        log.error(e.getMessage());
        return errorResponse;
    }

    // 防止被全局异常处理器覆盖
    @ExceptionHandler(AuthenticationException.class)
    public void error(AuthenticationException e) {
        throw e;
    }

    // 防止被全局异常处理器覆盖
    @ExceptionHandler(AccessDeniedException.class)
    public void error(AccessDeniedException e) {
        throw e;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ResponseResult<Void>> bizExceptionHandler(MethodArgumentNotValidException e) {
        List<ResponseResult<Void>> errorResponses = new ArrayList<>();
        e.printStackTrace();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            ResponseResult<Void> errorResponse = new ResponseResult<>();
            errorResponse.setCode(ExceptionType.BAD_REQUEST.getCode());
            errorResponse.setMessage(error.getDefaultMessage());
            errorResponses.add(errorResponse);
            log.error(error.getDefaultMessage());
        });
        return errorResponses;
    }
}
