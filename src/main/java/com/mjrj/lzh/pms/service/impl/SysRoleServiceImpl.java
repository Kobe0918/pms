package com.mjrj.lzh.pms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mjrj.lzh.pms.dao.SysRoleDOMapper;
import com.mjrj.lzh.pms.dao.SysRolePermissionRelationDOMapper;
import com.mjrj.lzh.pms.dto.pagedto.RolePageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.SysRoleDO;
import com.mjrj.lzh.pms.entity.SysRolePermissionRelationDO;
import com.mjrj.lzh.pms.service.BaseService;
import com.mjrj.lzh.pms.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-04-15 12:06
 * @Description: ${Description}
 */
@Service
@Slf4j
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDOMapper, SysRoleDO> implements SysRoleService<SysRoleDO> {

@Autowired
private SysRolePermissionRelationDOMapper relationDOMapper;

    @Override
    public List<SysRoleDO> getAll() {
        return mapper.getAll();
    }

//    设置状态需要在登入时判断 用户的职位状态和角色状态为可用 1。
    @Override
    public RolePageDTO<SysRoleDO> getRoleByPage(RolePageDTO dto) {
        int pageNo = dto.getStart() / dto.getLength() + 1;
        log.info(pageNo + ": pageNo");
        PageHelper.startPage(pageNo, dto.getLength());
        List<SysRoleDO> roleDOList = mapper.selectByPage(dto);
        PageInfo<SysRoleDO> pageInfo = new PageInfo<>(roleDOList);
        dto.setData(roleDOList);
        dto.setRecordsFiltered((int) pageInfo.getTotal());
        dto.setRecordsTotal((int) pageInfo.getTotal());
        dto.setDraw(dto.getDraw());
        return dto;
    }

    @Override
    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    public ResponseResult addRole(SysRoleDO sysRoleDO){
        sysRoleDO.setCreateTime(new Date());
        mapper.insertSelective(sysRoleDO);
        int roleId= sysRoleDO.getId();
        List<SysRolePermissionRelationDO> list = new ArrayList <>();

        Integer[] ids = sysRoleDO.getPermissionIds();
        for(Integer s : ids){
            SysRolePermissionRelationDO relationDO = new SysRolePermissionRelationDO();
            relationDO.setPermissionId(s);
            relationDO.setRoleId(roleId);
            list.add(relationDO);
        }
        relationDOMapper.insertForeach(list);
        return ResponseTool.success();
    }

    @Override
    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    public ResponseResult updateRole(SysRoleDO sysRoleDO) {
        sysRoleDO.setUpdateTime(new Date());
        mapper.updateByPrimaryKeySelective(sysRoleDO);
//       先删在插入
        relationDOMapper.deleteByRoleId(sysRoleDO.getId());
        int roleId= sysRoleDO.getId();
        List<SysRolePermissionRelationDO> list = new ArrayList <>();

        Integer[] ids = sysRoleDO.getPermissionIds();
        for(Integer s : ids){
            SysRolePermissionRelationDO relationDO = new SysRolePermissionRelationDO();
            relationDO.setPermissionId(s);
            relationDO.setRoleId(roleId);
            list.add(relationDO);
        }
        relationDOMapper.insertForeach(list);
        log.info(sysRoleDO.toString());
        log.info(sysRoleDO.getId()+":: id");
        return ResponseTool.success();
    }


    @Override
    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    public ResponseResult deleteByIds(List<Integer> ids){
        mapper.deleteByIds(ids);
        relationDOMapper.deleteByRoleIds(ids);
        return ResponseTool.success();
    }


}
