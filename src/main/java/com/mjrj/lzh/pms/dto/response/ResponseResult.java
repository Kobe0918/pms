package com.mjrj.lzh.pms.dto.response;

import com.mjrj.lzh.pms.springsecurity.response.ResultCode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.response
 * @Author: lzh
 * @CreateTime: 2020-03-31 15:42
 * @Description: ${Description}
 */
@Data
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 8678318667810887502L;
    private Boolean success;
    private Integer errorCode;
    private String errorMsg;
    private T data;


    public ResponseResult() {
    }

    public ResponseResult(Boolean success, Integer errorCode, String errorMsg) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ResponseResult(boolean success) {
        this.success = success;
        this.errorCode = success ? ResponseCode.SUCCESS.getCode() : ResponseCode.COMMON_FAIL.getCode();
        this.errorMsg = success ? ResponseCode.SUCCESS.getMessage() : ResponseCode.COMMON_FAIL.getMessage();
    }

    public ResponseResult(boolean success, ResponseCode resultEnum) {
        this.success = success;
        this.errorCode = success ? (resultEnum == null ? ResponseCode.SUCCESS.getCode()  : resultEnum.getCode()):ResponseCode.COMMON_FAIL.getCode();
        this.errorMsg = success ? (resultEnum == null ? ResponseCode.SUCCESS.getMessage()  : resultEnum.getMessage()):ResponseCode.COMMON_FAIL.getMessage();
    }

    public ResponseResult(boolean success, T data) {
        this.success = success;
        this.errorCode = success ? ResponseCode.SUCCESS.getCode() : ResponseCode.COMMON_FAIL.getCode();
        this.errorMsg = success ? ResponseCode.SUCCESS.getMessage() : ResponseCode.COMMON_FAIL.getMessage();
        this.data = data;
    }


    public ResponseResult(boolean success, ResponseCode resultEnum, T data) {
        this.success = success;
        this.errorCode = success ?  (resultEnum == null ? ResponseCode.SUCCESS.getCode()  : resultEnum.getCode()):ResponseCode.COMMON_FAIL.getCode();
        this.errorMsg = success ? (resultEnum == null ? ResponseCode.SUCCESS.getMessage()  : resultEnum.getMessage()):ResponseCode.COMMON_FAIL.getMessage();
        this.data = data;
    }


}
