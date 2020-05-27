package com.mjrj.lzh.pms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjrj.lzh.pms.entity.BaseDO;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto
 * @Author: lzh
 * @CreateTime: 2020-04-18 13:12
 * @Description: ${Description}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -8283792565045038553L;

    @NotNull(groups = ValidationGroups.UpdateUser.class,message = "请选择一条记录进行操作")
    private Integer id;
    @NotBlank(message = "真实姓名不能为空" ,groups = {ValidationGroups.Register.class, ValidationGroups.UpdateUser.class, ValidationGroups.AddUser.class} )
    @Length(min = 2, max = 20, message = "用户名应为2～20个字符" , groups = {ValidationGroups.Register.class})
    private String userName;
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
   @Email(message = "请输入正确的邮箱地址")
    private String email;

    private String workAddress;

    private String idCard;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    private Byte sex;
    private Byte isMarry;

    private String liveAddress;
    @NotNull(message = "部门为必填项", groups = {ValidationGroups.Register.class})
    private Integer deptId;
    @NotNull(message = "职位为必填项", groups = {ValidationGroups.UpdateUser.class,ValidationGroups.AddUser.class})
    private Integer positionId;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workTime;

    private Byte workStatus;

    private String province;
    private String country;
    private String city;

    private String imgUrl;
    private String deptName;
    private String positionName;

    @NotBlank(message = "用户密码不能为空", groups = {ValidationGroups.AddUser.class})
    @Length(min = 8, max = 15, message = "用户密码应为8～15个字符", groups = {ValidationGroups.AddUser.class})
   private String password;

    @NotBlank(message = "确认密码不能为空", groups = {ValidationGroups.AddUser.class})
    @Length(min = 8, max = 15, message = "用户密码应为8～15个字符", groups = {ValidationGroups.AddUser.class})
    private String checkPassword;
}
