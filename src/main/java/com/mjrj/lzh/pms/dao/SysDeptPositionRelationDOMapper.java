package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.SysDeptPositionRelationDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDeptPositionRelationDOMapper extends BaseMapper<SysDeptPositionRelationDO> {

    List<Integer> selectByDeptId(@Param("deptId")Integer deptId);

    @Delete(value = "delete  from sys_dept_position_relation where dept_id = #{deptId}")
    void deleteWithDept(@Param("deptId")Integer deptId);


    void deleteByDeptIdAndAnyPositionId(@Param("deptId")Integer deptId,@Param("positionIds")List<Integer> positionIds);


    void insertByDeptIdAndAnyPositionId(@Param("list")List<SysDeptPositionRelationDO> list);

    @Select(value = "select count(*) from sys_dept_position_relation where dept_id=#{deptId} and position_id = #{positionId}")
    int selectCuntByDeptPosition(@Param("deptId") Integer deptId,@Param("positionId") Integer positionId);
}