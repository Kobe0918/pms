package com.mjrj.lzh.pms.springsecurity.jwt;

import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.redis.RedisComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.jwt
 * @Author: lzh
 * @CreateTime: 2020-03-19 21:05
 * @Description: ${Description}
 */

@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private RedisComponent redisComponent;


    private String tokenHeader = "Authorization";

    private String tokenHead = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        //先从url中取token
        String authToken = request.getParameter("token");
        String authHeader = request.getHeader(this.tokenHeader);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(tokenHead)) {
            //如果header中存在token，则覆盖掉url中的token   "Bearer "之后的内容
            authToken = authHeader.substring(tokenHead.length());
        }

        if (StringUtils.hasText(authToken)) {
            String tokenKey = jwtTokenUtils.getTokenKeyFromToken(authToken);
            if (tokenKey != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("token key  : " + tokenKey);
                //从已有的user缓存中取了出user信息
                CurrentUserDTO currentUser = redisComponent.getLoginUser(tokenKey);
                //检查token是否有效
                if (currentUser != null && jwtTokenUtils.validateToken(authToken, currentUser)) {
//是否更新数据
//                    if(jwtTokenUtils.canTokenBeRefreshed(authToken)){
//                       String refreshedToken = jwtTokenUtils.refreshToken(authToken);
//
//                    }

                    if (!currentUser.getSysPermission().isEmpty()) {
                       log.info(currentUser.getSysPermission().size() + " : 权限");
                    } else {
                        log.info("用户没有权限");
                    }

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //设置用户登录状态
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

//    public  checkLoginTime(String authToken){
//         if(jwtTokenUtils.canTokenBeRefreshed(authToken)){
//
//         }
//    }


    public String getToken(HttpServletRequest request) {
        String authToken = null;
        String authHeader = request.getHeader(this.tokenHeader);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(tokenHead)) {
            //如果header中存在token，则覆盖掉url中的token   "Bearer "之后的内容
            authToken = authHeader.substring(tokenHead.length());
        }
        return authToken;
    }
}
