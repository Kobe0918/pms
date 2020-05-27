package com.mjrj.lzh.pms.springsecurity.service;


import com.fasterxml.jackson.databind.util.BeanUtil;
import com.mjrj.lzh.pms.dao.SysPermissionDOMapper;
import com.mjrj.lzh.pms.dao.SysUserDOMapper;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.entity.SysPermissionDO;
import com.mjrj.lzh.pms.entity.SysUserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.service
 * @Author: lzh
 * @CreateTime: 2020-03-18 14:11
 * @Description: ${Description}
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserDOMapper sysUserMapper;
    @Autowired
    private SysPermissionDOMapper sysPermissionMapper;
    // 验证手机号
    public static Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

//    验证用户  返回带权限集合的userDetail
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetailsServiceImpl 登入验证 : user " + username);
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("用户不能为空");
        }
        Matcher m = p.matcher(username);
        if(!m.matches()){
            throw new RuntimeException("手机号码格式有问题");
        }
        //根据用户名查询用户
        SysUserDO sysUser = sysUserMapper.selectUserByPhone(username);
        if (sysUser == null) {
            throw new RuntimeException("用户不存在");
        }
        CurrentUserDTO currentUser = new CurrentUserDTO();
//        需要对应的属性类型名完全一模一样
        BeanUtils.copyProperties(sysUser, currentUser);
        currentUser.setSysPermission(sysPermissionMapper.selectPermissionListByUserId(sysUser.getId()));
        List<SysPermissionDO> list = sysPermissionMapper.selectPermissionListByUserId(sysUser.getId());
        for(SysPermissionDO s : list){
            log.info(s.getPermission()+" -- ");
        }
        return currentUser;
    }
}
