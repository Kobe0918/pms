package com.mjrj.lzh.pms.util;

import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.entity.SysUserDO;
import com.mjrj.lzh.pms.springsecurity.jwt.JwtTokenUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.util
 * @Author: lzh
 * @CreateTime: 2020-03-27 19:58
 * @Description: ${Description}
 */
public class UserUtil {
    public static CurrentUserDTO getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                return null;
            }

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                CurrentUserDTO user = (CurrentUserDTO)authentication.getPrincipal();
                System.out.println(user.getId() + " :user");
                return user;
            }
        }
        System.out.println("为空");
        return null;
    }
}
