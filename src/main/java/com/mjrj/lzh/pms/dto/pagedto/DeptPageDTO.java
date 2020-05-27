package com.mjrj.lzh.pms.dto.pagedto;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.pagedto
 * @Author: lzh
 * @CreateTime: 2020-04-13 13:08
 * @Description: ${Description}
 */
@Data
public class DeptPageDTO<DeptDO> extends PageDTO<DeptDO> implements Serializable {
    private static final long serialVersionUID = 7936567342990771072L;

    private String deptName;

    private String orderColumn;

}
