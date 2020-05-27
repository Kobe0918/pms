package com.mjrj.lzh.pms.dto.pagedto;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.pagedto
 * @Author: lzh
 * @CreateTime: 2020-04-19 09:15
 * @Description: ${Description}
 */
@Data
public class UserPageDTO<SysUserDO> extends PageDTO<SysUserDO> implements Serializable {
    private static final long serialVersionUID = -8895017681790754960L;

    private String userName;

}
