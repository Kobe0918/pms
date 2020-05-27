package com.mjrj.lzh.pms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mjrj.lzh.pms.dao.LeaveDOMapper;
import com.mjrj.lzh.pms.dao.SysUserDOMapper;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.dto.LeaveDTO;
import com.mjrj.lzh.pms.dto.pagedto.LeavePageDTO;
import com.mjrj.lzh.pms.dto.pagedto.PageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.LeaveDO;
import com.mjrj.lzh.pms.entity.SysUserDO;
import com.mjrj.lzh.pms.service.LeaveService;
import com.mjrj.lzh.pms.service.SysUserService;
import com.mjrj.lzh.pms.util.UploadFileUtil;
import com.mjrj.lzh.pms.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-04-06 23:00
 * @Description: ${Description}
 */
@Service
@Slf4j
public class LeaveServiceImpl extends BaseServiceImpl <LeaveDOMapper, LeaveDO> implements LeaveService <LeaveDO> {

    @Autowired
    private UploadFileUtil uploadFileUtit;
    @Autowired
    private SysUserDOMapper userDOMapper;

    @Override
    public ResponseResult saveLeaveBySelf(LeaveDTO leave) {
        LeaveDO leaveDO = copyBeanUtil(leave);
        SysUserDO loginUser = UserUtil.getLoginUser();
        leaveDO.setUserId(loginUser.getId());
        leaveDO.setLeaveStatus((byte) 1);
        mapper.insertSelective(leaveDO);
        return ResponseTool.success();
    }


    @Override
    public LeavePageDTO<LeaveDO> getLeaveByUserWithPage(LeavePageDTO pageDTO) {
         SysUserDO loginUser = UserUtil.getLoginUser();
        if(pageDTO.getPersonnelId() != null){
            Integer deptId = userDOMapper.selectLeaveDeptId();
            if(!deptId .equals(loginUser.getDeptId())) {
                pageDTO.setMonitorId(loginUser.getId());
                pageDTO.setPersonnelId(null);
            }
        }

        int pageNo = pageDTO.getStart() / pageDTO.getLength() + 1;
        PageHelper.startPage(pageNo, pageDTO.getLength());
        List <LeaveDO> leaveList = mapper.selectByPage(pageDTO);
        log.info(String.valueOf(leaveList.size()) + " :size");
        PageInfo <LeaveDO> pageInfo = new PageInfo <>(leaveList);
        pageDTO.setData(leaveList);
        pageDTO.setRecordsFiltered((int) pageInfo.getTotal());
        pageDTO.setRecordsTotal((int) pageInfo.getTotal());
        pageDTO.setDraw(pageDTO.getDraw());
        log.info(pageDTO.toString());
        return pageDTO;
    }


    @Override
    public ResponseResult updateLeaveByUserSelf(LeaveDTO dto) {
        LeaveDO leaveDO = copyBeanUtil(dto);
        leaveDO.setLeaveStatus((byte) 1);
        mapper.updateByPrimaryKeySelective(leaveDO);
        return ResponseTool.success();
    }

    @Override
    public ResponseResult revokeLeaveBySelf(List <Integer> leaveId) {
        mapper.updateStatus(leaveId);
        return ResponseTool.success();
    }

    @Override
    public ResponseResult deleteFileWhenUpdate(LeaveDTO dto, HttpServletRequest request) {

//        删除数据
        String leaveImgUrl = mapper.selectByLeaveId(dto.getLeaveId());
        if (!StringUtils.isEmpty(leaveImgUrl)) {
            String[] imgs = leaveImgUrl.split(",");
            String path = "";
            for (String s : imgs) {
                if (!s.endsWith(dto.getPath())) {
                    path = s;
                }
            }
//        修改数据库
            int i = mapper.updateImgUrl(dto.getLeaveId(), path);
            //   删除文件
            if (i > 0) {
                return uploadFileUtit.deleteFileByPath(dto.getPath(), request);
            }
        }
        return ResponseTool.fail();
    }


    public LeaveDO copyBeanUtil(LeaveDTO dto) {
        StringBuffer stringBuffer = new StringBuffer();
        LeaveDO leaveDO = new LeaveDO();
        BeanUtils.copyProperties(dto, leaveDO);
        if (dto.getImgUrl() != null && dto.getImgUrl().size() > 0) {
            int i = dto.getImgUrl().size();
            for (int j = 0; j < i; j++) {
                stringBuffer.append(dto.getImgUrl().get(j));
                if (j != i - 1) {
                    stringBuffer.append(",");
                }
            }
            leaveDO.setLeaveImgUrl(stringBuffer.toString());
        }
        return leaveDO;
    }


    @Override
    public ResponseResult passLeave(List <Integer> ids){
        Byte status ;
        LeaveDO leaveDO = mapper.selectByPrimaryKey(ids.get(0));
        SysUserDO loginUser = (SysUserDO)UserUtil.getLoginUser();
        if(loginUser.getId() .equals( leaveDO.getPersonnelId())){
            status = 5;
        }else{
            status = 2;
        }
        mapper.updateleaveStatusByIds(status,ids);
        return ResponseTool.success();
    }


    @Override
    public ResponseResult UnpassLeave(List <Integer> ids) {
        Byte status  = 4;
        mapper.updateleaveStatusByIds(status,ids);
        return ResponseTool.success();
    }
}
