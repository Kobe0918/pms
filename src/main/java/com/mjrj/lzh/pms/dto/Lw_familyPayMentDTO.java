package com.mjrj.lzh.pms.dto;

import java.io.Serializable;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto
 * @Author: lzh
 * @CreateTime: 2020-06-12 14:13
 * @Description: ${Description}
 */
public class Lw_familyPayMentDTO implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private	java.math.BigDecimal	baz612	;	//	消费人家庭共济成员ID
    private	java.math.BigDecimal	baz611	;	//	消费人家庭共济ID
    private	java.math.BigDecimal	aac001_cb;		//	消费人个人编号
    private	String	aab299;		//	消费人行政区号
    private	String	aac003;		//	消费人姓名
    private	String	aac002;	//	消费人身份证号
    private	java.math.BigDecimal	aae058;		//	消费金额
    private	String	baz105;		//	变更类型编号
    private	String	baz106;		//	变更类型名称
    private	String	aae072;		//	结算单据号
    private	String	bab107;		//	费用变更类型
    private	String	bkc001;		//	人员待遇享受类别
    private	String	akb020;		//	服务机构编号
    private	String	aab301_wd;		//	网点行政区号
    private	String	aab034_wd;		//	网点分中心编号
    private	String	aae013;		    //	备注
    private java.math.BigDecimal  bkc075;         // 共济账户支付总额
    private java.math.BigDecimal  bkeg12;         // 他人共济支付额
    private java.math.BigDecimal  ake034;         // 个人共济账户支付额
    private java.math.BigDecimal  bkc040;         // 个人现金支付额
    private String  code;           // 标志
    private String  message;            //消息


    public java.math.BigDecimal getBkc075() {
        return bkc075;
    }
    public void setBkc075(java.math.BigDecimal bkc075) {
        this.bkc075 = bkc075;
    }
    public java.math.BigDecimal getBkeg12() {
        return bkeg12;
    }
    public void setBkeg12(java.math.BigDecimal bkeg12) {
        this.bkeg12 = bkeg12;
    }
    public java.math.BigDecimal getAke034() {
        return ake034;
    }
    public void setAke034(java.math.BigDecimal ake034) {
        this.ake034 = ake034;
    }
    public java.math.BigDecimal getBkc040() {
        return bkc040;
    }
    public void setBkc040(java.math.BigDecimal bkc040) {
        this.bkc040 = bkc040;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public java.math.BigDecimal getBaz612() {
        return baz612;
    }
    public void setBaz612(java.math.BigDecimal baz612) {
        this.baz612 = baz612;
    }
    public java.math.BigDecimal getBaz611() {
        return baz611;
    }
    public void setBaz611(java.math.BigDecimal baz611) {
        this.baz611 = baz611;
    }
    public java.math.BigDecimal getAac001_cb() {
        return aac001_cb;
    }
    public void setAac001_cb(java.math.BigDecimal aac001Cb) {
        aac001_cb = aac001Cb;
    }
    public String getAab299() {
        return aab299;
    }
    public void setAab299(String aab299) {
        this.aab299 = aab299;
    }
    public String getAac003() {
        return aac003;
    }
    public void setAac003(String aac003) {
        this.aac003 = aac003;
    }
    public String getAac002() {
        return aac002;
    }
    public void setAac002(String aac002) {
        this.aac002 = aac002;
    }
    public java.math.BigDecimal getAae058() {
        return aae058;
    }
    public void setAae058(java.math.BigDecimal aae058) {
        this.aae058 = aae058;
    }
    public String getBaz105() {
        return baz105;
    }
    public void setBaz105(String baz105) {
        this.baz105 = baz105;
    }
    public String getBaz106() {
        return baz106;
    }
    public void setBaz106(String baz106) {
        this.baz106 = baz106;
    }
    public String getAae072() {
        return aae072;
    }
    public void setAae072(String aae072) {
        this.aae072 = aae072;
    }
    public String getBab107() {
        return bab107;
    }
    public void setBab107(String bab107) {
        this.bab107 = bab107;
    }
    public String getBkc001() {
        return bkc001;
    }
    public void setBkc001(String bkc001) {
        this.bkc001 = bkc001;
    }
    public String getAkb020() {
        return akb020;
    }
    public void setAkb020(String akb020) {
        this.akb020 = akb020;
    }
    public String getAab301_wd() {
        return aab301_wd;
    }
    public void setAab301_wd(String aab301Wd) {
        aab301_wd = aab301Wd;
    }
    public String getAab034_wd() {
        return aab034_wd;
    }
    public void setAab034_wd(String aab034Wd) {
        aab034_wd = aab034Wd;
    }
    public String getAae013() {
        return aae013;
    }
    public void setAae013(String aae013) {
        this.aae013 = aae013;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
