package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.dto.pagedto.PageDTO;
import com.mjrj.lzh.pms.entity.LeaveDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveDOMapper extends BaseMapper<LeaveDO>{

    List<LeaveDO>  selectUserByPage(PageDTO pageDTO);

    @Select(value="select leave_img_url from leave_history where leave_id = #{id}")
    String selectByLeaveId(@Param("id") Integer id);

    @Update(value = "update leave_history set leave_img_url = #{path} , update_time = now() where leave_id = #{id}")
    int updateImgUrl(@Param("id")Integer id, @Param("path")String path);

    void updateleaveStatusByIds(@Param("status") Byte status,@Param("list") List<Integer> list);
}