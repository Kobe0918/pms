package com.mjrj.lzh.pms.dao;

import com.google.common.annotations.VisibleForTesting;
import com.mjrj.lzh.pms.dto.pagedto.DeptPageDTO;
import com.mjrj.lzh.pms.entity.DeptDO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface DeptDOMapper extends BaseMapper<DeptDO> {
 @Select(value = "select * from dept order by  #{columnName} desc")
    List<DeptDO>  selectTest(@Param("columnName")String columnName);

    List<DeptDO>  selectDeptByPage(DeptPageDTO dto);

    @Insert(value = "insert into sys_dept_position_relation(dept_id,position_id) values(#{deptId},#{positionId})")
    void insertIntoSDPR(@Param("deptId")Integer deptId,@Param("positionId")Integer positionId);


    @Select(value="select * from dept where dept_status = #{deptStatus} and instr(dept_name,#{deptName})>0")
    List<DeptDO> selectDeptByName(@Param("deptStatus") Boolean deptStatus,@Param("deptName")String deptName);
}