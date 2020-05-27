package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.dto.pagedto.RolePageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.SysRoleDO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-04-15 12:06
 * @Description: ${Description}
 */
public interface SysRoleService<T> extends BaseService<T>{
     List<SysRoleDO> getAll();

     RolePageDTO<SysRoleDO> getRoleByPage(RolePageDTO dto);


    ResponseResult addRole(SysRoleDO sysRoleDO);

    ResponseResult updateRole(SysRoleDO sysRoleDO);

    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    ResponseResult deleteByIds(List <Integer> ids);
}
