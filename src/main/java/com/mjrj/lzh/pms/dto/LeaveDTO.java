package com.mjrj.lzh.pms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjrj.lzh.pms.validate.Image;
import com.mjrj.lzh.pms.validate.StringTime;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto
 * @Author: lzh
 * @CreateTime: 2020-04-06 20:23
 * @Description: ${Description}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDTO implements Serializable {
    private static final long serialVersionUID = 8769683996960727201L;

    @NotNull(groups = {ValidationGroups.UpdateLeave.class}, message = "选择一条请假记录，进行操作")
    private Integer leaveId;

    private String path;


    @NotNull(groups = {ValidationGroups.Common.class},message = "请假类型为必填项!")
    private Byte leaveStyle;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    private String leaveReason;
    private List<String> imgUrl;
    @NotNull(groups = {ValidationGroups.Common.class},message = "部门主管为必填项")
    private Integer monitorId;
    @NotNull(groups = {ValidationGroups.Common.class},message = "人事抄送为必填项")
    private Integer personnelId;



}
