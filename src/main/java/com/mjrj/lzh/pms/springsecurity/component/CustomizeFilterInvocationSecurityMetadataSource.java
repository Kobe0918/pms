package com.mjrj.lzh.pms.springsecurity.component;

import com.mjrj.lzh.pms.dao.SysPermissionDOMapper;
import com.mjrj.lzh.pms.entity.SysPermissionDO;
import com.mjrj.lzh.pms.redis.RedisComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.component
 * @Author: lzh
 * @CreateTime: 2020-03-18 16:31
 * @Description: ${安全数据源}
 */
@Slf4j
//@Component
public class CustomizeFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    SysPermissionDOMapper sysPermissionMapper;
    @Autowired
    private RedisComponent redisComponent;

    //获取路径对应的全部权限
    @Override
    public Collection <ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        List <String> staticPath = redisComponent.getStaticPathByRedisT("static_path");
        for (String s : staticPath) {
            if (requestUrl.startsWith(s)) {
                return null;
            }
        }
        //查询具体某个接口的权限  已被修改selectPermissionListByUrl 没有用,使用报错
        List <SysPermissionDO> permissionList = sysPermissionMapper.selectPermissionListByUrl(requestUrl);
        if (permissionList == null || permissionList.size() == 0) {
            //请求路径没有配置权限，表明该请求接口可以任意访问
            return null;
        }
        String[] attributes = new String[permissionList.size()];
        int size = permissionList.size();
        for (int i = 0; i < size; i++) {
            attributes[i] = permissionList.get(i).getPermission();
        }
        log.info("enter medasource");
        return SecurityConfig.createList(attributes);
    }


    @Override
    public Collection <ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    //    特别注意需要将默认的false改为true
    @Override
    public boolean supports(Class <?> aClass) { return true; }
}
