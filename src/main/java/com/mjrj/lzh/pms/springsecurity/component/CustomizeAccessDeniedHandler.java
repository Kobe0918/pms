package com.mjrj.lzh.pms.springsecurity.component;

import com.alibaba.fastjson.JSON;

import com.mjrj.lzh.pms.springsecurity.response.JsonResult;
import com.mjrj.lzh.pms.springsecurity.response.ResultCode;
import com.mjrj.lzh.pms.springsecurity.response.ResultTool;
import com.mjrj.lzh.pms.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.component
 * @Author: lzh
 * @CreateTime: 2020-03-19 02:04
 * @Description: ${Description}
 */
@Component
@Slf4j
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        log.info("无权 CustomizeAccessDeniedHandler");
//        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

        JsonResult result = ResultTool.fail(ResultCode.NO_PERMISSION);

        ResponseUtil.responseJson(httpServletResponse, HttpStatus.FORBIDDEN.value(),result);
    }
}
