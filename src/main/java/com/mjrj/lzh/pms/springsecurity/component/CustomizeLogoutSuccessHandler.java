package com.mjrj.lzh.pms.springsecurity.component;

import com.alibaba.fastjson.JSON;
import com.mjrj.lzh.pms.redis.RedisComponent;
import com.mjrj.lzh.pms.springsecurity.jwt.JwtAuthenticationTokenFilter;
import com.mjrj.lzh.pms.springsecurity.jwt.JwtTokenUtils;
import com.mjrj.lzh.pms.springsecurity.response.JsonResult;
import com.mjrj.lzh.pms.springsecurity.response.ResultTool;
import com.mjrj.lzh.pms.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.component
 * @Author: lzh
 * @CreateTime: 2020-03-18 15:21
 * @Description: ${处理logout的逻辑}
 */
@Component
@Slf4j
public class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private JwtAuthenticationTokenFilter tokenFilter;
    @Autowired
    private JwtTokenUtils tokenUtils;
    @Autowired
    private RedisComponent redisComponent;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("logout 成功处理");
        String token = tokenFilter.getToken(httpServletRequest);
//        redis 中key是user中的token  UUid
        String tokenKeyFromToken = tokenUtils.getTokenKeyFromToken(token);
        redisComponent.deleteUser(tokenKeyFromToken);
        JsonResult result = ResultTool.success();
        ResponseUtil.responseJson(httpServletResponse, HttpStatus.OK.value(),result);

    }

}
