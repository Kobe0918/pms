package com.mjrj.lzh.pms.controller;

import com.alibaba.fastjson.JSONArray;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.SysPermissionDO;
import com.mjrj.lzh.pms.service.PermissionService;
import com.mjrj.lzh.pms.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-04-11 15:54
 * @Description: ${Description}
 */
@RestController
@RequestMapping("/permissions")
@Slf4j
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/current")
    public List <SysPermissionDO> permissionsCurrent() {
        CurrentUserDTO loginUser = UserUtil.getLoginUser();
        final List <SysPermissionDO> sysPermissionDOS = loginUser.getSysPermission();

        final List <SysPermissionDO> permissions = sysPermissionDOS.stream().filter(l -> l.getType().equals(true)).collect(Collectors.toList());

        List <SysPermissionDO> firstLevel = permissions.stream().filter(l -> (l.getParentId()==0)).collect(Collectors.toList());
        firstLevel.parallelStream().forEach(l -> {
            setChild(l, permissions);
        });
        log.info(firstLevel.size() + " : size");
        return firstLevel;
    }

    private void setChild(SysPermissionDO p, List <SysPermissionDO> permissions) {
        List <SysPermissionDO> child = permissions.parallelStream().filter(a -> a.getParentId().equals(p.getId())).collect(Collectors.toList());
        p.setChild(child);
        if (!CollectionUtils.isEmpty(child)) {
            child.parallelStream().forEach(c -> {
                //递归设置子元素，多级菜单支持
                setChild(c, permissions);
            });
        }
    }


    @GetMapping("/getUserAuthority")
    public ResponseResult getUserAuthority(){
        CurrentUserDTO loginUser = UserUtil.getLoginUser();
        List <SysPermissionDO> sysPermissionDOS = loginUser.getSysPermission().parallelStream().filter(a -> "sys:leave:edit".equals(a.getPermission())).collect(Collectors.toList());
        if(sysPermissionDOS.size() > 0 ){
            return ResponseTool.success(loginUser.getId());
        }else{
            return ResponseTool.fail(loginUser.getId());
        }
    }


    @GetMapping("/selectAll")
    public JSONArray selectAll(){
        return permissionService.selectAll();
    }

    @GetMapping("/selectAllByTreeGrid")
    public  ResponseResult selectAllByTreeGrid(){
        return permissionService.getAll();
    }
}
