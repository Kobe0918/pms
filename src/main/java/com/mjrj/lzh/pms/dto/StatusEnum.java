package com.mjrj.lzh.pms.dto;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.util
 * @Author: lzh
 * @CreateTime: 2020-03-17 20:26
 * @Description: ${Description}
 */
public enum StatusEnum {

    INNER_ERROR(-1, "系统异常"),
//    register
    VALIDATE_FAIL(0,"数据校验失败"),
    VERIFY_FAIL(1,"验证码校验失败"),
    EXIT_USER(2,"用户已注册"),
    REGISTER_SUCCESS(3,"注册成功，请登入"),
//    login
    USER_NOTEXIT(6,"用户不存在，请先注册"),
    WRONG_PWD(7,"密码错误！"),
    LOGIN_SUCCESS(8,"欢迎来到敏捷OA办公"),
//    发送短信
    SEND_SUCCESS(4,"正在发送短信"),
    SEND_FAIL(5,"正在进行操作，请勿频繁发送短信验证码"),
    ;

    Integer state;
    String stateInfo;

    StatusEnum(){ }

    StatusEnum(Integer state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public static StatusEnum indexOf(Integer state){
        for(StatusEnum result : values()){
            if(result.getState().equals(state)){
                return result;
            }
        }
        return null;
    }

}
