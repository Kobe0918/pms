package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.SysLogsDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysLogsDOMapper extends BaseMapper<SysLogsDO>{

    void  deleteByIds(List<Integer> list);

    @Delete(value="delete from sys_logs where createTime < #{time}")
    int deleteLogs(@Param("time") String time);
}