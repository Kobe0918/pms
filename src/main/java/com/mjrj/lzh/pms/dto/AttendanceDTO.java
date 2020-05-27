package com.mjrj.lzh.pms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto
 * @Author: lzh
 * @CreateTime: 2020-04-02 12:56
 * @Description: ${Description}
 */
@Data
public class AttendanceDTO implements Serializable {
    private static final long serialVersionUID = 480587401507322505L;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date takeTime;

    private String place;

    private String remark;

    private String nowPlace;

    private Boolean isException;

    private String status;


    public AttendanceDTO() {
    }

    public AttendanceDTO(String nowPlace, Boolean isException) {
        this.nowPlace = nowPlace;
        this.isException = isException;
    }




}
