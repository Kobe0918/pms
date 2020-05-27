package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.SysRoleDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SysRoleDOMapper extends BaseMapper<SysRoleDO>{

    @Select(value = "select * from sys_role")
    public List<SysRoleDO> getAll();

    void deleteByIds( @Param("ids") List<Integer> ids);
}