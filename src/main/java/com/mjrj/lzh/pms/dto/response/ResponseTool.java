package com.mjrj.lzh.pms.dto.response;



/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.response
 * @Author: lzh
 * @CreateTime: 2020-03-31 15:43
 * @Description: ${Description}
 */
public class ResponseTool {
    public static ResponseResult success() {
        return new ResponseResult(true);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult <>(true, data);
    }

    public static ResponseResult success(ResponseCode responseEnum) {
        return new ResponseResult(true,responseEnum);
    }

    public static ResponseResult fail() {
        return new ResponseResult(false);
    }

    public static ResponseResult fail(ResponseCode responseEnum) {
        return new ResponseResult(false, responseEnum);
    }

    public static <T>ResponseResult<T> fail(T data) {
        return new ResponseResult(false, data);
    }

    public static <T> ResponseResult<T>  fail(ResponseCode responseEnum,T data) {
        return new ResponseResult(false, responseEnum,data);
    }

    public static <T> ResponseResult<T> success(ResponseCode responseEnum,T data) {
        return new ResponseResult(true, responseEnum,data);
    }

}
