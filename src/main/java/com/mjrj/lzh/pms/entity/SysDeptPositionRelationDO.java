package com.mjrj.lzh.pms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDeptPositionRelationDO extends BaseDO<Integer>{


    private static final long serialVersionUID = 5315819272950501675L;
    private Integer deptId;

    private Integer positionId;


}