package com.mjrj.lzh.pms.springsecurity.component;

import com.alibaba.fastjson.JSON;
import com.mjrj.lzh.pms.dao.SysUserDOMapper;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.entity.SysLogsDO;
import com.mjrj.lzh.pms.entity.SysUserDO;
import com.mjrj.lzh.pms.redis.RedisComponent;
import com.mjrj.lzh.pms.service.SysLogsService;
import com.mjrj.lzh.pms.service.SysUserService;
import com.mjrj.lzh.pms.springsecurity.jwt.JwtTokenUtils;
import com.mjrj.lzh.pms.springsecurity.response.JsonResult;
import com.mjrj.lzh.pms.springsecurity.response.ResultTool;
import com.mjrj.lzh.pms.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.component
 * @Author: lzh
 * @CreateTime: 2020-03-18 15:05
 * @Description: ${处理登入成功以后的逻辑}
 */
@Component
@Slf4j
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtTokenUtils tokenUtils;
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private SysLogsService sysLogsService;
    @Autowired
    private SysUserService sysUserService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("登入成功处理  CustomizeAuthenticationSuccessHandler");
        CurrentUserDTO user = (CurrentUserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setToken(UUID.randomUUID().toString());
        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展
        //返回json数据

        //jwt  创建token可以保存到redis中
        final String token = tokenUtils.generateToken(user);
        redisComponent.cacheLoginUser(user.getToken(),user);
        sysLogsService.insert(new SysLogsDO(user.getId(),"登入",(byte) 1,new Date(),null,null));
//        验证工作状态
        sysUserService.loginSuccessProcedure(user.getId());
        JsonResult result = ResultTool.success(token);

        ResponseUtil.responseJson(httpServletResponse, HttpStatus.OK.value(),result);
    }
}
