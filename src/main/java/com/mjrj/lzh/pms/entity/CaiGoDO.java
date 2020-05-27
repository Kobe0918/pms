package com.mjrj.lzh.pms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class CaiGoDO {
    @NotNull(groups = ValidationGroups.UpdateCaigo.class,message = "选择一条记录进行操作")
    private Integer caigoId;
    @NotBlank(groups = ValidationGroups.Common.class,message = "请输入名称")
    private String caigoName;
    @NotBlank(groups = ValidationGroups.Common.class,message = "请输入规格")
    private String caigoSize;
    @NotNull(groups = ValidationGroups.Common.class,message = "请输入数量")
    private Integer caigoNumber;
    @NotNull(groups = ValidationGroups.Common.class,message = "请输入总金额")
    private Float caigoMoney;

    private String caigoRemark;

    private Integer caigoUserId;
    private String caigoUserName;
    @NotNull(groups = ValidationGroups.Common.class,message = "请选择审批人")
    private Integer caigoSpiuserId;
    private String caigoSpiUserName;
    private Byte caigoStatus;

    @NotNull(groups = ValidationGroups.Common.class,message = "请输入采购事由")
    private String caigoReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date caigoCreateTime;
}