package com.mjrj.lzh.pms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class PmsConfigDO {
    private Integer id;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date attendanceBeginTime;
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date attendanceEndTime;

    private Double attendanceX1;

    private Double attendanceY1;

    private Double attendanceX2;

    private Double attendanceY2;

    private Integer leaveDeptId;
    private String leaveDeptName;

    private Integer caigoSpiuserId;
    private String caigoSpiuserName;

    private Integer initPositionId;
    private String  initPositionNmae;

    private String logCron;

    private String attendanceCron;


}