package com.mjrj.lzh.pms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class PositionDO implements Serializable {

    private static final long serialVersionUID = -8078740278956953730L;
    @NotNull(groups = {ValidationGroups.UpdatePosition.class},message = "请选择一条记录进行操作")
    private Integer positionId;
    @NotNull(groups = {ValidationGroups.Common.class},message = "请选择一条记录进行操作")
    private String positionName;

    private Boolean positionStatus;

    private String remark;

    private Integer updateUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer deptId;
    @NotNull(groups = {ValidationGroups.Common.class},message = "职位等级为必填项")
    private Byte positionSort;

    private List<Integer>  roleIds;

    private List<Integer> oldRoleId;

    private String updateUserName;

    public PositionDO(Integer positionId, Boolean positionStatus, Integer updateUserId, Date updateTime) {
        this.positionId = positionId;
        this.positionStatus = positionStatus;
        this.updateUserId = updateUserId;
        this.updateTime = updateTime;
    }
}