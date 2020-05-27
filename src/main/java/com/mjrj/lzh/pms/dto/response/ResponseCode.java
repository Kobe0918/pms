package com.mjrj.lzh.pms.dto.response;

import com.mjrj.lzh.pms.springsecurity.response.ResultCode;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.response
 * @Author: lzh
 * @CreateTime: 2020-03-31 15:44
 * @Description: ${Description}
 */
public enum ResponseCode {

    /* 成功 */
    SUCCESS(200, "成功"),

    /* 默认失败 */
    COMMON_FAIL(999, "失败"),

    VALIDATE_FAIL(998,"数据校验失败"),


    UP_DAKA(250,"上班打卡"),
    F_UP_DOWN(260,"下班打卡"),
    DOWN_DAKA(270,"下班打卡"),
    F_DOWN_DAKA(280,"上班忘记打卡,下班打卡了"),
    F_DAKA(220,"早上或下午忘记打卡"),
    WITHOD_MSG(290,"没有打卡数据"),
    IMG_NULL(310,"请上传图片"),
    SYSTEM_ERROR(100,"系统故障");



    private Integer code;
    private String message;

     ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResponseCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
