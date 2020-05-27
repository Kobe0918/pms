package com.mjrj.lzh.pms.dto;

import lombok.Data;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto
 * @Author: lzh
 * @CreateTime: 2020-03-17 22:14
 * @Description: ${Description}
 */
@Data
public class ResponseDTO{
    private Integer code;
    private String msg;

    public ResponseDTO() {
    }

    public ResponseDTO(StatusEnum e) {
        this.code = e.getState();
        this.msg = e.getStateInfo();
    }




}
