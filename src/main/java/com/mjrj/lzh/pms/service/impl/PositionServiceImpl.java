package com.mjrj.lzh.pms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mjrj.lzh.pms.dao.PositionDOMapper;
import com.mjrj.lzh.pms.dao.SysPositionRoleRelationDOMapper;
import com.mjrj.lzh.pms.dto.pagedto.PositionPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.dto.response.Select2DTO;
import com.mjrj.lzh.pms.entity.PositionDO;
import com.mjrj.lzh.pms.entity.SysPositionRoleRelationDO;
import com.mjrj.lzh.pms.service.PositionService;
import com.mjrj.lzh.pms.util.CommonUtil;
import com.mjrj.lzh.pms.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-04-13 12:57
 * @Description: ${Description}
 */
@Service
@Slf4j
public class PositionServiceImpl extends BaseServiceImpl <PositionDOMapper, PositionDO> implements PositionService <PositionDO> {

    @Autowired
    private SysPositionRoleRelationDOMapper sprrMapper;

    @Override
    public ResponseResult selectAllBySelect2() {
        List <PositionDO> positionDOS = mapper.selectAllBySelect2(true);
        List <Select2DTO> list = new ArrayList <>();
        positionDOS.forEach(p -> {
            list.add(new Select2DTO(p.getPositionId(), p.getPositionName()));
        });
        return ResponseTool.success(list);
    }

    @Override
    public ResponseResult selectPositionBySearch(String positionName){
        List <PositionDO> positionDOS = mapper.selectPositionByName(true,positionName);
        List<Select2DTO> select2DTOS = new ArrayList <>();
        positionDOS.forEach(u->{
            select2DTOS.add(new Select2DTO(u.getPositionId(),u.getPositionName()));
        });
        return ResponseTool.success(select2DTOS);
    }

    @Override
    public PositionPageDTO <PositionDO> getPositionByPage(PositionPageDTO dto) {
        int pageNo = dto.getStart() / dto.getLength() + 1;

        PageHelper.startPage(pageNo, dto.getLength());
        List <PositionDO> positionList = mapper.selectByPage(dto);
        PageInfo <PositionDO> pageInfo = new PageInfo <>(positionList);

        dto.setData(positionList);
        dto.setRecordsFiltered((int) pageInfo.getTotal());
        dto.setRecordsTotal((int) pageInfo.getTotal());
        dto.setDraw(dto.getDraw());
        return dto;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public ResponseResult savePosition(PositionDO positionDO) {
        positionDO.setCreateTime(new Date());
        positionDO.setPositionStatus(true);
        mapper.insertSelective(positionDO);
        Integer positionid = positionDO.getPositionId();
        List <Integer> roleIds = positionDO.getRoleIds();
        if (!CommonUtil.isEmpty(roleIds)) {
            List <SysPositionRoleRelationDO> list = new ArrayList <>();
            roleIds.forEach(r -> {
                list.add(new SysPositionRoleRelationDO(positionid, r));
            });
            sprrMapper.insertList(list);
        }
        return ResponseTool.success();
    }


    @Override
    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    public ResponseResult updatePosition(PositionDO positionDO) {
        log.info(positionDO.toString() + " : 修改");
        positionDO.setUpdateTime(new Date());
        positionDO.setUpdateUserId(UserUtil.getLoginUser().getId());
        mapper.updateByPrimaryKeySelective(positionDO);
        final int positionId = positionDO.getPositionId();
        final List <Integer> newRoleIds = positionDO.getRoleIds();
        final List <Integer> oldRoleIds = positionDO.getOldRoleId();

        if (CommonUtil.isEmpty(newRoleIds) && CommonUtil.isEmpty(oldRoleIds)) {
            return ResponseTool.success();
        }
//        单纯的删除
        if (CommonUtil.isEmpty(newRoleIds) && oldRoleIds.size() > 0) {
            sprrMapper.deleteByPositionIdAndAnyRoleId(positionId, oldRoleIds);
            return ResponseTool.success();
        }
        //        单纯的插入
        if (CommonUtil.isEmpty(oldRoleIds) && newRoleIds.size() > 0) {
            List <SysPositionRoleRelationDO> list = new ArrayList <>();
            for (Integer i : newRoleIds) {
                list.add(new SysPositionRoleRelationDO(positionId, i));
            }
            sprrMapper.insertList(list);
            return ResponseTool.success();
        }

//        既有删除又有插入
        List<SysPositionRoleRelationDO> list = new ArrayList <>();
        for(Integer i : newRoleIds){
            if(!oldRoleIds.contains(i)){
                list.add(new SysPositionRoleRelationDO(positionId,i));
            }
        }

        if(list.size() > 0){
            for(SysPositionRoleRelationDO i : list){
                log.info(i.getRoleId().toString() + " : insert ");
            }
            sprrMapper.insertList(list);
        }


        List<Integer> deleteList = new ArrayList <>();
        for(Integer i : oldRoleIds){
            if(!newRoleIds.contains(i)){
                deleteList.add(i);
            }
        }
        if(deleteList.size() > 0){
            for(Integer i : deleteList){
                log.info(i  + " : delete");
            }
            sprrMapper.deleteByPositionIdAndAnyRoleId(positionId,deleteList);
        }

        return ResponseTool.success();
    }


    @Override
    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    public ResponseResult deletePosition(Integer positionId) {
        mapper.deleteByPrimaryKey(positionId);
        sprrMapper.deleteByPositionId(positionId);
        return ResponseTool.success();
    }


}
