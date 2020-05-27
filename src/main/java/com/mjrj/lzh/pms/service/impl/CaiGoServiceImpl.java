package com.mjrj.lzh.pms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mjrj.lzh.pms.dao.CaiGoDOMapper;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.dto.pagedto.CaigoPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.CaiGoDO;
import com.mjrj.lzh.pms.service.BaseService;
import com.mjrj.lzh.pms.service.CaiGoService;
import com.mjrj.lzh.pms.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-04-16 20:58
 * @Description: ${Description}
 */
@Service
@Slf4j
public class CaiGoServiceImpl extends BaseServiceImpl <CaiGoDOMapper, CaiGoDO> implements CaiGoService <CaiGoDO> {


    @Override
    public CaigoPageDTO selectCaiGoWithPage(CaigoPageDTO dto) {
        CurrentUserDTO loginUser = UserUtil.getLoginUser();
        if(dto.getRole() != null && "user".equals(dto.getRole())){
            dto.setCaigoUserId(loginUser.getId());
        }
        if(dto.getCheckUser() != null && "*124.259zh".equals( dto.getCheckUser())){
            dto.setCaigoSpiuserId(loginUser.getId());
            dto.setCaigoStatus((byte)1);
        }
        int pageNo = dto.getStart() / dto.getLength() + 1;
        log.info(pageNo + ": pageNo");
        PageHelper.startPage(pageNo, dto.getLength());
        List <CaiGoDO> list = mapper.selectByPage(dto);
        PageInfo <CaiGoDO> pageInfo = new PageInfo <>(list);
        for(CaiGoDO s: list){
            log.info(s.getCaigoStatus().toString());
        }
        dto.setData(list);
        dto.setRecordsFiltered((int) pageInfo.getTotal());
        dto.setRecordsTotal((int) pageInfo.getTotal());
        dto.setDraw(dto.getDraw());
        return dto;
    }


    @Override
    public ResponseResult revokeCaigoStatus(Byte status,List <Integer> ids) {
        mapper.updateCaigoStatus(status,ids);
        return ResponseTool.success();
    }
}
