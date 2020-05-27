package com.mjrj.lzh.pms.springsecurity.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.component
 * @Author: lzh
 * @CreateTime: 2020-03-18 16:43
 * @Description: ${访问决策管理器AccessDecisionManager}
 */
//@Component
@Slf4j
public class CustomizeAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        Iterator<ConfigAttribute> iterator = collection.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute ca = iterator.next();
            //当前请求需要的权限
            String needRole = ca.getAttribute();
            //当前用户所具有的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足!");

    }

//    特别注意需要将默认的false改为true
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }
    //    特别注意需要将默认的false改为true
    @Override
    public boolean supports(Class <?> aClass) {
        return true;
    }
}
