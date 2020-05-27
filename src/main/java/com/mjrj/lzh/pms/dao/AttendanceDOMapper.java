package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.AttendanceDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceDOMapper extends BaseMapper<AttendanceDO>{


    List<AttendanceDO> selectByUserIdAndTakeTime(@Param("userId") Integer userId, @Param("time") String time);



}