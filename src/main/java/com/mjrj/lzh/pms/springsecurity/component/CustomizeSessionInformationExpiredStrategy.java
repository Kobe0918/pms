package com.mjrj.lzh.pms.springsecurity.component;

import com.alibaba.fastjson.JSON;
import com.mjrj.lzh.pms.springsecurity.response.JsonResult;
import com.mjrj.lzh.pms.springsecurity.response.ResultCode;
import com.mjrj.lzh.pms.springsecurity.response.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.component
 * @Author: lzh
 * @CreateTime: 2020-03-18 15:48
 * @Description: ${处理单人登入的逻辑}
 */
//@Component
@Slf4j
public class CustomizeSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        log.info("限制用户登入只能一个");
        JsonResult result = ResultTool.fail(ResultCode.USER_ACCOUNT_USE_BY_OTHERS);
        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
