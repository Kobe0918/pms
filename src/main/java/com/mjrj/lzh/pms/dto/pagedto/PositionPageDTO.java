package com.mjrj.lzh.pms.dto.pagedto;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.pagedto
 * @Author: lzh
 * @CreateTime: 2020-04-15 10:38
 * @Description: ${Description}
 */
@Data
public class PositionPageDTO<PositionDO> extends PageDTO<PositionDO> implements Serializable {
    private static final long serialVersionUID = -4835590988595229797L;

    private String positionName;
}
