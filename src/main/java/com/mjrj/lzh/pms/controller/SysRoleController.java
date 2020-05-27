package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.annotation.LogAnnotation;
import com.mjrj.lzh.pms.dao.SysRolePermissionRelationDOMapper;
import com.mjrj.lzh.pms.dto.pagedto.RolePageDTO;
import com.mjrj.lzh.pms.dto.pagedto.SysLogsPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.SysLogsDO;
import com.mjrj.lzh.pms.entity.SysRoleDO;
import com.mjrj.lzh.pms.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-05-02 15:19
 * @Description: ${Description}
 */
@RestController
@RequestMapping("/roles")
@Slf4j
public class SysRoleController {


    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysRolePermissionRelationDOMapper rolePermissionRelationDOMapper;


    @PostMapping("/getRolesWithPage")
    public RolePageDTO<SysRoleDO> selectByPage(@Valid @RequestBody RolePageDTO dto, BindingResult result) {
        ResponseResult msg = LeaveController.getErrorMsg(result);
        if (msg.getSuccess()) {
            return   roleService.getRoleByPage(dto);
        }
        return  null;
    }

    @LogAnnotation(module = "添加角色")
    @PostMapping("/addRole")
    public ResponseResult addRole(@RequestBody @Valid SysRoleDO roleDO, BindingResult result){
        ResponseResult msg = LeaveController.getErrorMsg(result);
        if (msg.getSuccess()) {
            return   roleService.addRole(roleDO);
        }
        return  msg;
    }

    @LogAnnotation(module = "修改角色信息")
    @PostMapping("/editRole")
    public ResponseResult editRole(@RequestBody @Valid SysRoleDO roleDO, BindingResult result){
        ResponseResult msg = LeaveController.getErrorMsg(result);
        if (msg.getSuccess()) {
            return   roleService.updateRole(roleDO);
        }
        return  msg;
    }

    @LogAnnotation(module = "删除角色")
    @DeleteMapping("/deleteRoles")
    public ResponseResult deleteRoles(@RequestBody List<Integer> ids){
        if(!CollectionUtils.isEmpty(ids)){
            return roleService.deleteByIds(ids);
        }
        return new ResponseResult(false,300,"请选择数据进行操作");
    }

    @GetMapping("{id}")
    public  List<Integer> get(@PathVariable Integer id) {
        return rolePermissionRelationDOMapper.selectByRoleIds(id);
    }

}
