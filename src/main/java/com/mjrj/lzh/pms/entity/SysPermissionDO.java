package com.mjrj.lzh.pms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysPermissionDO extends BaseDO<Integer>{

    private static final long serialVersionUID = -6525908145032868837L;

    private Integer parentId;

    private String name;

    private String css;

    private String href;

    private Boolean type;

    private String permission;

    private Integer sort;

    private List<SysPermissionDO> child;

}