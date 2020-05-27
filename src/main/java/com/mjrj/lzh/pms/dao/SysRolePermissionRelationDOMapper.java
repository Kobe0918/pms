package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.SysRolePermissionRelationDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRolePermissionRelationDOMapper extends BaseMapper<SysRolePermissionRelationDO>{

   void insertForeach(@Param("doList") List<SysRolePermissionRelationDO> list);

   void deleteByIds(@Param("roleId")Integer roleId,@Param("permissionIds")List<Integer> positionIds);

   void deleteByRoleId(@Param("roleId")Integer roleId);

   void deleteByRoleIds(@Param("roleIds") List<Integer> roleIds);

   @Select("select permission_id from sys_role_PERMISSION_relation where role_id = #{roleId}")
   List<Integer> selectByRoleIds(@Param("roleId")Integer roleId);
}