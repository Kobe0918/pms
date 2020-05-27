package com.mjrj.lzh.pms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mjrj.lzh.pms.entity.SysPermissionDO;
import com.mjrj.lzh.pms.entity.SysUserDO;
import lombok.Data;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto
 * @Author: lzh
 * @CreateTime: 2020-03-27 13:06
 * @Description: ${Description}
 */

public class CurrentUserDTO  extends SysUserDO implements  UserDetails{

    private static final long serialVersionUID = 6203051638650181606L;

    private String token;

    private List<SysPermissionDO> sysPermission;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List <SysPermissionDO> getSysPermission() {
        return sysPermission;
    }

    public void setSysPermission(List <SysPermissionDO> sysPermission) {
        this.sysPermission = sysPermission;
    }

    @JsonIgnore
    @Override
    public Collection <? extends GrantedAuthority> getAuthorities() {
        try{
            Collection <SimpleGrantedAuthority> collect = sysPermission.parallelStream().filter(p -> !StringUtils.isEmpty(p.getPermission()))
                    .map(p -> new SimpleGrantedAuthority(p.getPermission())).collect(Collectors.toSet());
            return collect;
        }catch(Exception e){
            throw new DisabledException("账号暂无权限，联系管理员处理");
        }
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return getTelephone();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return getNotExpired();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return getAccountNotLocked();
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return getCredentialsNotExpired();
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return getEnabled();
    }


    @Override
    public String toString() {
        return "CurrentUserDTO{" +
                "token='" + token + '\'' +
                ", sysPermission=" + sysPermission +
                '}';
    }
}
