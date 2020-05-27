package com.mjrj.lzh.pms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjrj.lzh.pms.validate.Exception;
import com.mjrj.lzh.pms.validate.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDO extends BaseDO<Integer>{

    private static final long serialVersionUID = 7817746451823263740L;
    private Integer userId;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date takeTime;
    @NotBlank(message = "打卡地址不能为空")
    private String place;

    private Double workHours;

    private String remark;
    @Status()
    private Byte status;
    @Exception()
    private Boolean isException;



    public AttendanceDO(Integer userId, Date takeTime, String place, String remark, Byte status) {
        this.userId = userId;
        this.takeTime = takeTime;
        this.place = place;
        this.remark = remark;
        this.status = status;
    }
}