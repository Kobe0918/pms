package com.mjrj.lzh.pms.entity;

import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @author linzehang
 */

@Data
public class UserDO implements Serializable {
    /**
     * 1. 在controller校验没加分组的时候，只对实体类的没有分组的注解有效。
     * 2. 在controller校验加分组的时候，只对实体类的当前分组的注解有效，没有注解的也无效。
     * 3. 当校验有两个分组的时候@Validated({AddGroup.class, UpdateGroup.class})，满足当前两个分组其中任意一个都可以校验，两个注解同时一起出现，也没问题，而且检验不通过的信息不会重复。
     */

    @Null(groups = {ValidationGroups.Register.class})
    private BigDecimal id;
    @NotBlank(message = "真实姓名不能为空" ,groups = {ValidationGroups.Register.class} )
    @Length(min = 2, max = 20, message = "用户名应为2～20个字符" , groups = {ValidationGroups.Register.class})
    private String name;
    @NotBlank(message = "手机号码不能为空", groups = {ValidationGroups.Common.class})
    @Pattern(regexp = "^1([345789])\\d{9}$" , message = "输入手机格式不正确" , groups = {ValidationGroups.Common.class})
    private String telephone;
    @NotBlank(message = "用户密码不能为空", groups = {ValidationGroups.Common.class})
    @Length(min = 8, max = 15, message = "用户密码应为8～15个字符", groups = {ValidationGroups.Common.class})
    private String password;
    @Email
    private String email;

    private String workAddress;

    private String workStatus;

    private String idCard;

    private Integer birthday;

    private String sex;

    private String nation;

    private String familyAddress;

    private String liveAddress;

    private String isMarry;

    private Integer workTime;

    private String education;

    private Integer graduationTime;

    private String bankCard;

    private String deptId;

    private Integer positionId;

    private Date createTime;

    private String status;

    private String entrystatus;

    private Integer syonTime;

    private Integer correctionTime;
    @NotBlank(message = "短信验证码不能为空", groups = {ValidationGroups.Register.class})
    @Length(min = 6, max = 6, message = "短信验证码为6位", groups = {ValidationGroups.Register.class})
    @Pattern(regexp = "^\\d{6}$", message = "请输入阿拉伯数字", groups = {ValidationGroups.Register.class})
    private String verify;



    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress == null ? null : workAddress.trim();
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus == null ? null : workStatus.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public Integer getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress == null ? null : familyAddress.trim();
    }

    public String getLiveAddress() {
        return liveAddress;
    }

    public void setLiveAddress(String liveAddress) {
        this.liveAddress = liveAddress == null ? null : liveAddress.trim();
    }

    public String getIsMarry() {
        return isMarry;
    }

    public void setIsMarry(String isMarry) {
        this.isMarry = isMarry == null ? null : isMarry.trim();
    }

    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    public Integer getGraduationTime() {
        return graduationTime;
    }

    public void setGraduationTime(Integer graduationTime) {
        this.graduationTime = graduationTime;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard == null ? null : bankCard.trim();
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getEntrystatus() {
        return entrystatus;
    }

    public void setEntrystatus(String entrystatus) {
        this.entrystatus = entrystatus == null ? null : entrystatus.trim();
    }

    public Integer getSyonTime() {
        return syonTime;
    }

    public void setSyonTime(Integer syonTime) {
        this.syonTime = syonTime;
    }

    public Integer getCorrectionTime() {
        return correctionTime;
    }

    public void setCorrectionTime(Integer correctionTime) {
        this.correctionTime = correctionTime;
    }
}