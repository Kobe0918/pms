package com.mjrj.lzh.pms.springsecurity.component;



import com.alibaba.fastjson.JSON;

import com.mjrj.lzh.pms.springsecurity.response.JsonResult;
import com.mjrj.lzh.pms.springsecurity.response.ResultCode;
import com.mjrj.lzh.pms.springsecurity.response.ResultTool;
import com.mjrj.lzh.pms.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.component
 * @Author: lzh
 * @CreateTime: 2020-03-18 14:52
 * @Description: ${处理异常请求（如未登入，登入过期等请求）}
 */
@Component
@Slf4j
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        //没登入 统一的json格式
        JsonResult jsonResult = ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        ResponseUtil.responseJson(httpServletResponse, HttpStatus.UNAUTHORIZED.value(),jsonResult);
    }
}
