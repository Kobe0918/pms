package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.AttendanceDO;

import java.text.ParseException;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-03-31 14:58
 * @Description: ${Description}
 */
public interface AttendanceService<T> extends BaseService<T> {

    ResponseResult getDakaMessage(String time) throws  Exception;

    ResponseResult getDakaAddress(String ip) throws Exception;

    ResponseResult saveDakaMessage(AttendanceDO attendance);

}
