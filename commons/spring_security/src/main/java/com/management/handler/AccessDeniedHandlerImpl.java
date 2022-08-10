package com.management.handler;

import com.alibaba.fastjson.JSON;
import com.management.common.ResponseResult;
import com.management.exception.ExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 夏明
 * @version 1.0
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value()); // 设置HTTP响应码403

        ResponseResult errorResponse = new ResponseResult(ExceptionType.FORBIDDEN.getCode(), "您的权限不足");
        String json = JSON.toJSONString(errorResponse);
        response.getWriter().println(json);
        response.getWriter().flush();
    }
}
