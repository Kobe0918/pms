package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.dto.pagedto.PositionPageDTO;
import com.mjrj.lzh.pms.entity.DeptDO;
import com.mjrj.lzh.pms.entity.PositionDO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionDOMapper  extends BaseMapper<PositionDO>{

    @Select(value = "select * from position where position_status = #{status} order by position_sort asc")
    List<PositionDO> selectAllBySelect2(@Param("status") Boolean status);

    @Select(value="select * from position where position_status = #{status} and instr(position_name,#{name})>0")
    List<PositionDO> selectPositionByName(@Param("status") Boolean status,@Param("name") String name);


}