package com.mjrj.lzh.pms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysPositionRoleRelationDO extends BaseDO<Integer>{

    private static final long serialVersionUID = 4218495592167610193L;

    private Integer positionId;

    private Integer roleId;

}