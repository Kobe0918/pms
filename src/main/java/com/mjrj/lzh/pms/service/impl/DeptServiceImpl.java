package com.mjrj.lzh.pms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mjrj.lzh.pms.dao.DeptDOMapper;
import com.mjrj.lzh.pms.dao.SysDeptPositionRelationDOMapper;
import com.mjrj.lzh.pms.dto.pagedto.DeptPageDTO;
import com.mjrj.lzh.pms.dto.pagedto.PageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.dto.response.Select2DTO;
import com.mjrj.lzh.pms.entity.DeptDO;
import com.mjrj.lzh.pms.entity.LeaveDO;
import com.mjrj.lzh.pms.entity.SysDeptPositionRelationDO;
import com.mjrj.lzh.pms.service.DeptService;
import com.mjrj.lzh.pms.util.CommonUtil;
import com.mjrj.lzh.pms.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-04-13 12:56
 * @Description: ${Description}
 */
@Service
@Slf4j
public class DeptServiceImpl extends BaseServiceImpl<DeptDOMapper, DeptDO> implements DeptService<DeptDO> {
    @Autowired
    private SysDeptPositionRelationDOMapper dprMapper;
    @Override
    public DeptPageDTO<DeptDO> getDeptByPage(DeptPageDTO dto){
        int pageNo = dto.getStart() / dto.getLength() + 1;
        log.info(pageNo + ": pageNo");
        PageHelper.startPage(pageNo, dto.getLength());
        List<DeptDO> deptList = mapper.selectByPage(dto);
        PageInfo<DeptDO> pageInfo = new PageInfo <>(deptList);
        dto.setData(deptList);
        dto.setRecordsFiltered((int) pageInfo.getTotal());
        dto.setRecordsTotal((int) pageInfo.getTotal());
        dto.setDraw(dto.getDraw());
        return dto;
    }



    @Override
    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    public ResponseResult saveDept(DeptDO deptDO) {
        deptDO.setCreateTime(new Date());
        deptDO.setDeptStatus(true);
        deptDO.setDeptStatus(true);
        mapper.insertSelective(deptDO);
        List <Integer> positionId = deptDO.getPositionId();
        if(!CommonUtil.isEmpty(positionId)){
//            if(!positionId.contains(1)){
//                positionId.add(1);
//            }
            positionId.forEach(p->{
                mapper.insertIntoSDPR(deptDO.getDeptId(),p);
            });
        }
        return ResponseTool.success();
    }


    @Override
    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    public ResponseResult deleteDept(Integer deptId){
        mapper.deleteByPrimaryKey(deptId);
        dprMapper.deleteWithDept(deptId);
        return ResponseTool.success();
    }


    @Override
    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    public ResponseResult updateDept(DeptDO deptDO){
        log.info(deptDO.toString() + " : 修改");
        deptDO.setUpdateTime(new Date());
        deptDO.setUpdateUserId(UserUtil.getLoginUser().getId());
        mapper.updateByPrimaryKeySelective(deptDO);
       final int deptId= deptDO.getDeptId();
       final List<Integer> newPositionId = deptDO.getPositionId();
       final List<Integer> oldPositionId = deptDO.getOldPositionId();

        if(CommonUtil.isEmpty(newPositionId) && CommonUtil.isEmpty(oldPositionId)){
            return ResponseTool.success();
        }
//        单纯的删除
        if(CommonUtil.isEmpty(newPositionId) && oldPositionId.size() >0){
            dprMapper.deleteByDeptIdAndAnyPositionId(deptId,oldPositionId);
            return ResponseTool.success();
        }
        //        单纯的插入
        if(CommonUtil.isEmpty(oldPositionId) && newPositionId.size() >0){
            List<SysDeptPositionRelationDO> list = new ArrayList <>();
            for(Integer  i : newPositionId){
                list.add(new SysDeptPositionRelationDO(deptId,i));
            }
            dprMapper.insertByDeptIdAndAnyPositionId(list);
            return ResponseTool.success();
        }

// 插入又有删除
//        方法一：
//        //用来存放两个数组中相同的元素
//        Set<Integer> same = new HashSet<Integer>();
//        //用来存放数组a中的元素
//        Set<Integer> temp = new HashSet<Integer>();
//
//        for(int  i=0; i < newPositionId.size();i++ ){
//          temp.add(newPositionId.get(i));
//        }
//        for(int j = 0 ; j< oldPositionId.size();j++){
//            /*添加不进去代表有key  相同数据*/
//            if(!temp.add(oldPositionId.get(j))){
//                same.add(oldPositionId.get(j));
//            }
//        }
//        Iterator<Integer> it1 = oldPositionId.iterator();
//        while(it1.hasNext()){
//            if(same.contains(it1.next())){
//                it1.remove();
//            }
//        }
//        做删除
//        dprMapper.deleteByDeptIdAndAnyPositionId(deptDO.getDeptId(),oldPositionId);
//
////       做插入
//        List<SysDeptPositionRelationDO> list = new ArrayList <>();
//        Iterator<Integer> it2 = newPositionId.iterator();
//        while(it2.hasNext()){
//            if(!same.contains(it2.next())){
//                list.add(new SysDeptPositionRelationDO(deptId,it2.next()));
//            }
//        }
//        dprMapper.insertByDeptIdAndAnyPositionId(list);


//        方法二：
        List<SysDeptPositionRelationDO> list = new ArrayList <>();
        for(Integer i : newPositionId){
            if(!oldPositionId.contains(i)){
                list.add(new SysDeptPositionRelationDO(deptId,i));
            }
        }

        if(list.size() > 0){
            for(SysDeptPositionRelationDO i : list){
                log.info(i.getPositionId().toString() + " : insert ");
            }
            dprMapper.insertByDeptIdAndAnyPositionId(list);
        }


        List<Integer> deleteList = new ArrayList <>();
        for(Integer i : oldPositionId){
            if(!newPositionId.contains(i)){
                deleteList.add(i);
            }
        }
        if(deleteList.size() > 0){
            for(Integer i : deleteList){
                log.info(i  + " : delete");
            }
            dprMapper.deleteByDeptIdAndAnyPositionId(deptId,deleteList);
        }


        return ResponseTool.success();
    }

    @Override
    public ResponseResult getDeptByName(String deptName) {
        List <DeptDO> deptDOS = mapper.selectDeptByName(true,deptName);
        List<Select2DTO> select2DTOS = new ArrayList <>();
        deptDOS.forEach(u->{
            select2DTOS.add(new Select2DTO(u.getDeptId(),u.getDeptName()));
        });
        return ResponseTool.success(select2DTOS);
    }
}
