package com.mjrj.lzh.pms.service.impl;

import com.mjrj.lzh.pms.dao.AttendanceDOMapper;
import com.mjrj.lzh.pms.dao.PmsConfigDOMapper;
import com.mjrj.lzh.pms.dto.AttendanceDTO;
import com.mjrj.lzh.pms.dto.response.ResponseCode;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.AttendanceDO;
import com.mjrj.lzh.pms.entity.PmsConfigDO;
import com.mjrj.lzh.pms.entity.SysUserDO;
import com.mjrj.lzh.pms.service.AttendanceService;
import com.mjrj.lzh.pms.service.BaseService;
import com.mjrj.lzh.pms.util.CommonUtil;
import com.mjrj.lzh.pms.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-03-31 14:57
 * @Description: ${Description}
 */

@Service
@Slf4j
public class AttendanceServiceImpl extends BaseServiceImpl<AttendanceDOMapper,AttendanceDO> implements AttendanceService<AttendanceDO> {

    @Autowired
    private PmsConfigDOMapper pmsConfigDOMapper;

    @Override
    public ResponseResult getDakaMessage(String time) throws Exception {
        time = !StringUtils.isEmpty(time) ? time : CommonUtil.getCurrentDate();
        log.info(time);

        // 改 4-5 0.40 修改
        List <AttendanceDO> attendance = mapper.selectByUserIdAndTakeTime(UserUtil.getLoginUser().getId(), time);
        if (attendance.size() == 0) {
            if (CommonUtil.compareToCurrentTime(time)) {
                return ResponseTool.success(ResponseCode.UP_DAKA);
            }
            if (CommonUtil.afterFiverClock(time)) {
                return ResponseTool.success(ResponseCode.F_UP_DOWN);
            }
        }

        if (attendance.size() == 1) {
            if (CommonUtil.compareTime(time)) {
                if (attendance.get(0).getStatus() == 1) {
                    return ResponseTool.success(ResponseCode.DOWN_DAKA, attendance);
                }
                return ResponseTool.success(ResponseCode.F_DOWN_DAKA, attendance);
            }
            if (!CommonUtil.compareTime(time)) {
                return ResponseTool.success(ResponseCode.F_DAKA, attendance);
            }
        }

        if (attendance.size() == 2) {
            return ResponseTool.success(attendance);
        }

        return ResponseTool.success(ResponseCode.WITHOD_MSG);

    }


    @Override
    public ResponseResult getDakaAddress(String ip) throws Exception {
        Map <String, Double> latitude = CommonUtil.getLatitude(ip);
        String address = CommonUtil.getAddrByBaiduApi(latitude).getData().toString();
        if (inRange(latitude)) {
            return ResponseTool.success(new AttendanceDTO(address, true));
        } else {
            return ResponseTool.success(new AttendanceDTO(address, false));
        }
    }


    @Override
    public ResponseResult saveDakaMessage(AttendanceDO attendance) {
        SysUserDO loginUser = UserUtil.getLoginUser();
        attendance.setTakeTime(new Date());
        attendance.setUserId(loginUser.getId());
        mapper.insertSelective(attendance);
        return ResponseTool.success();
    }




    private boolean inRange(Map <String, Double> latitude) {
        //    后期改成管理员设定  获取地址  范围即可
//         5-10 改
          PmsConfigDO pmsConfigDO = pmsConfigDOMapper.selectConfig(1);
          double x1 = pmsConfigDO.getAttendanceX1();
          double x2 = pmsConfigDO.getAttendanceX2();
          double y1 = pmsConfigDO.getAttendanceY1();
          double y2 = pmsConfigDO.getAttendanceY2();
        return (latitude.get("x") > x1 && latitude.get("x") < x2 && latitude.get("y") > y1 && latitude.get("y") < y2);
    }

}
