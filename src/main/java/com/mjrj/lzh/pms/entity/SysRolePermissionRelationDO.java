package com.mjrj.lzh.pms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRolePermissionRelationDO extends BaseDO<Integer>{
    private static final long serialVersionUID = -4417715614021482064L;

    private Integer roleId;

    private Integer permissionId;

}