package com.mjrj.lzh.pms.dto.pagedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.Page;
import com.mjrj.lzh.pms.entity.SysLogsDO;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.pagedto
 * @Author: lzh
 * @CreateTime: 2020-04-20 14:38
 * @Description: ${Description}
 */
@Data
public class SysLogsPageDTO<SysLogsDO> extends PageDTO<SysLogsDO> {

    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

}
