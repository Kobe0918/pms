package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.dto.pagedto.DeptPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.DeptDO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-04-13 12:56
 * @Description: ${Description}
 */
public interface DeptService<T> extends BaseService<T> {
    DeptPageDTO<DeptDO> getDeptByPage(DeptPageDTO dto);

    ResponseResult saveDept(DeptDO deptDO);

    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    ResponseResult deleteDept(Integer deptId);

    @Transactional(rollbackFor={Exception.class},propagation = Propagation.REQUIRED)
    ResponseResult updateDept(DeptDO deptDO);

    ResponseResult getDeptByName(String deptName);


}
