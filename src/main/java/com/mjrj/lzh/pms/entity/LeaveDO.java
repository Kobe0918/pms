package com.mjrj.lzh.pms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDO implements Serializable {
    private static final long serialVersionUID = 7053421142220651713L;

    private Integer leaveId;

    private Integer userId;

    private String userName;
    private String deptName;

    private Byte leaveStyle;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private String leaveReason;

    private Integer monitorId;

    private Integer personnelId;

    private Byte leaveStatus;

    private String leaveImgUrl;

    private List<String> imgUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public void setLeaveImgUrl(String leaveImgUrl) {
        this.leaveImgUrl = leaveImgUrl;
        this.imgUrl = new ArrayList <>();
        String[] img = leaveImgUrl.split(",");
        for(String i : img){
            this.imgUrl.add(i);
        }
    }

    @Override
    public String toString() {
        return "LeaveDO{" +
                "leaveId=" + leaveId +
                ", leaveStyle=" + leaveStyle +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", leaveReason='" + leaveReason + '\'' +
                ", monitorId=" + monitorId +
                ", personnelId=" + personnelId +
                ", leaveStatus=" + leaveStatus +
                ", leaveImgUrl='" + leaveImgUrl + '\'' +
                '}';
    }
}