package com.mjrj.lzh.pms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class SysUserDO  extends BaseDO<Integer>{

    private static final long serialVersionUID = -7809315432127036583L;

    @NotBlank(message = "真实姓名不能为空" ,groups = {ValidationGroups.Register.class} )
    @Length(min = 2, max = 20, message = "用户名应为2～20个字符" , groups = {ValidationGroups.Register.class})
    private String userName;
    @NotBlank(message = "用户密码不能为空", groups = {ValidationGroups.Common.class})
    @Length(min = 8, max = 15, message = "用户密码应为8～15个字符", groups = {ValidationGroups.Common.class})
    private String password;
    @NotBlank(message = "用户联系方式为必填项", groups = {ValidationGroups.Common.class})
    @Pattern(regexp = "^1([345789])\\d{9}$" , message = "输入手机格式不正确" , groups = {ValidationGroups.Common.class})
    private String telephone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    private Boolean enabled;

    private Boolean notExpired;

    private Boolean accountNotLocked;

    private Boolean credentialsNotExpired;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer createUser;

    private Integer updateUser;

    private String email;

    private String workAddress;

    private String idCard;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    private Byte workStatus;

    private Byte sex;

    private Byte nation;

    private String familyAddress;

    private String liveAddress;

    private Byte isMarry;

    private Byte education;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date graduationTime;
    @NotNull(message = "部门为必填项", groups = {ValidationGroups.Register.class})
    private Integer deptId;

    private Integer positionId;

    private Byte entryStatus;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date syonTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date correctionTime;

    @NotBlank(message = "短信验证码不能为空", groups = {ValidationGroups.Common.class})
    @Length(min = 6, max = 6, message = "短信验证码为6位", groups = {ValidationGroups.Register.class})
    @Pattern(regexp = "^\\d{6}$", message = "请输入阿拉伯数字", groups = {ValidationGroups.Register.class})
    private String verify;

    @NotBlank(message = "确认密码不能为空", groups = {ValidationGroups.Forgot.class})
    @Length(min = 8, max = 15, message = "用户密码应为8～15个字符", groups = {ValidationGroups.Common.class})
    private String checkPassword;

    private String deptName;
    private String positionName;

    private String imgUrl;
}