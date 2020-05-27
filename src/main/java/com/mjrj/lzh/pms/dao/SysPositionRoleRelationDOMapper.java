package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.PositionDO;
import com.mjrj.lzh.pms.entity.SysPositionRoleRelationDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysPositionRoleRelationDOMapper extends BaseMapper<SysPositionRoleRelationDO>{

    void insertList(@Param("list") List<SysPositionRoleRelationDO> list );

    @Delete(value = "delete from sys_position_role_relation where position_id = #{positionId}")
    void deleteByPositionId(@Param("positionId") Integer positionId);

    void deleteByPositionIdAndAnyRoleId(@Param("positionId")Integer positionId,@Param("list")List<Integer> oldRoleIds);

}