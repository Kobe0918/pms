package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.dto.LeaveDTO;
import com.mjrj.lzh.pms.dto.pagedto.LeavePageDTO;
import com.mjrj.lzh.pms.dto.pagedto.PageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.LeaveDO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-04-06 23:00
 * @Description: ${Description}
 */
public interface LeaveService<T> extends BaseService <T> {
    ResponseResult saveLeaveBySelf(LeaveDTO leave);

    LeavePageDTO <LeaveDO> getLeaveByUserWithPage(LeavePageDTO pageDTO);

    ResponseResult updateLeaveByUserSelf(LeaveDTO dto);

    ResponseResult revokeLeaveBySelf(List <Integer> leaveId);

    ResponseResult deleteFileWhenUpdate(LeaveDTO dto, HttpServletRequest request);

    ResponseResult passLeave(List <Integer> ids);
    ResponseResult UnpassLeave(List <Integer> ids);

}
