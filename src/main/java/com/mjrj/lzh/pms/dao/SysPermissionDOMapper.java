package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.SysPermissionDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysPermissionDOMapper extends BaseMapper<SysPermissionDO>{

    List<SysPermissionDO> selectPermissionListByUserId(@Param("userId") Integer userId);

    List<SysPermissionDO> selectPermissionListByUrl(String url );

//    @Select(value = "select a.* from sys_permission a left join sys_role_permission_relation  b on( a.id = b.permission_id)where b.role_id = #{id}")
    List<SysPermissionDO> selectPermissionByRoleId(@Param("roleId") Integer roleId);

    List<SysPermissionDO> selectAll();

    List<SysPermissionDO> getAll();
}