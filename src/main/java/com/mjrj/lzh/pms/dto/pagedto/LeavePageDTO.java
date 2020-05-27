package com.mjrj.lzh.pms.dto.pagedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjrj.lzh.pms.entity.LeaveDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.pagedto
 * @Author: lzh
 * @CreateTime: 2020-04-13 13:04
 * @Description: ${Description}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeavePageDTO<LeaveDO> extends PageDTO<LeaveDO> implements Serializable {
    private static final long serialVersionUID = 8029177639196403557L;

    private String userName;

    private Integer userId;

    private Byte leaveStatus;

    private Integer personnelId;
    private Integer monitorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private String msg;


    public LeavePageDTO(String msg) {
        this.msg = msg;
    }
}
