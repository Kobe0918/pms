package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-04-15 14:13
 * @Description: ${Description}
 */

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private SysRoleService roleService;

    @GetMapping("/getRole")
    public ResponseResult getRole(){
        return ResponseTool.success(roleService.getAll()) ;
    }

}
