package com.mjrj.lzh.pms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mjrj.lzh.pms.dao.SysLogsDOMapper;
import com.mjrj.lzh.pms.dto.pagedto.SysLogsPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.SysLogsDO;
import com.mjrj.lzh.pms.service.BaseService;
import com.mjrj.lzh.pms.service.SysLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-03-27 16:59
 * @Description: ${Description}
 */
@Service
@Slf4j
public class SysLogsServiceImpl extends BaseServiceImpl<SysLogsDOMapper,SysLogsDO> implements SysLogsService<SysLogsDO> {

    @Async
    @Override
    public void saveLog(SysLogsDO logsDO){
        if(logsDO == null || StringUtils.isEmpty(logsDO.getUserId())){
            return ;
        }
        mapper.insertSelective(logsDO);
    }


    @Override
    public SysLogsPageDTO<SysLogsDO> getLogsByPage(SysLogsPageDTO dto) {
        int pageNo = dto.getStart() / dto.getLength() + 1;
        log.info(pageNo + ": pageNo");
        PageHelper.startPage(pageNo, dto.getLength());
        List<SysLogsDO> sysLogsDOList = mapper.selectByPage(dto);
        PageInfo<SysLogsDO> pageInfo = new PageInfo <>(sysLogsDOList);
        dto.setData(sysLogsDOList);
        dto.setRecordsFiltered((int) pageInfo.getTotal());
        dto.setRecordsTotal((int) pageInfo.getTotal());
        dto.setDraw(dto.getDraw());
        return dto;
    }


    @Override
    public ResponseResult deleteByIds(List <Integer> ids) {
        mapper.deleteByIds(ids);
        return ResponseTool.success();
    }


}
