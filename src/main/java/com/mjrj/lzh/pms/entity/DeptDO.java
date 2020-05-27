package com.mjrj.lzh.pms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class DeptDO implements Serializable {
    private static final long serialVersionUID = -3616058255094588619L;

    @NotNull(groups = {ValidationGroups.UpdateDept.class},message = "请选择一条记录进行操作")
    private Integer deptId;
    @NotEmpty(groups = {ValidationGroups.Common.class},message = "部门名称为必填选项")
    private String deptName;

    private Boolean deptStatus;

    private String remark;

    private List<Integer> positionId;

    private Integer updateUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer connectUserId;
    private SysUserDO  connectUser;

    private List<Integer> oldPositionId;


    public DeptDO(Integer deptId, Boolean deptStatus, Integer updateUserId, Date updateTime) {
        this.deptId = deptId;
        this.deptStatus = deptStatus;
        this.updateUserId = updateUserId;
        this.updateTime = updateTime;
    }


}