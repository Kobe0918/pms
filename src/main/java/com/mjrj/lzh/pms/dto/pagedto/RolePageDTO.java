package com.mjrj.lzh.pms.dto.pagedto;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.pagedto
 * @Author: lzh
 * @CreateTime: 2020-05-02 15:30
 * @Description: ${Description}
 */
@Data
public class RolePageDTO<SysRoleDO> extends PageDTO<SysRoleDO> implements Serializable {
    private static final long serialVersionUID = -2433846196414533193L;

    private String roleName;

}
