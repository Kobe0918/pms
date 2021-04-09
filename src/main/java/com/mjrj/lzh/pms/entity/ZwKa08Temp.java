package com.mjrj.lzh.pms.entity;


import cc.ebatis.annotation.Mapping;

public class ZwKa08Temp {
    @Mapping(key = "统筹区编号(必填)")
    private String aaa027;
    @Mapping(key = "身份证号(必填)")
    private String aac002;
    @Mapping(key = "姓名(必填)")
    private String aac003;
    @Mapping(key ="起始日期")
    private Long aae030;
    @Mapping(key = "截止日期")
    private Long aae031;
    @Mapping(key = "特殊病种名称(必填)")
    private String bke043;
    @Mapping(key = "数据更新时间")
    private String aae035;

    public String getAaa027() {
        return aaa027;
    }

    public void setAaa027(String aaa027) {
        this.aaa027 = aaa027 == null ? null : aaa027.trim();
    }

    public String getAac002() {
        return aac002;
    }

    public void setAac002(String aac002) {
        this.aac002 = aac002 == null ? null : aac002.trim();
    }

    public String getAac003() {
        return aac003;
    }

    public void setAac003(String aac003) {
        this.aac003 = aac003 == null ? null : aac003.trim();
    }

    public Long getAae030() {
        return aae030;
    }

    public void setAae030(Long aae030) {
        this.aae030 = aae030;
    }

    public Long getAae031() {
        return aae031;
    }

    public void setAae031(Long aae031) {
        this.aae031 = aae031;
    }

    public String getBke043() {
        return bke043;
    }

    public void setBke043(String bke043) {
        this.bke043 = bke043 == null ? null : bke043.trim();
    }

    public String getAae035() {
        return aae035;
    }

    public void setAae035(String aae035) {
        this.aae035 = aae035;
    }

    @Override
    public String toString() {
        return "ZwKa08Temp{" +
                "aaa027='" + aaa027 + '\'' +
                ", aac002='" + aac002 + '\'' +
                ", aac003='" + aac003 + '\'' +
                ", aae030=" + aae030 +
                ", aae031=" + aae031 +
                ", bke043='" + bke043 + '\'' +
                ", aae035='" + aae035 + '\'' +
                '}';
    }
}