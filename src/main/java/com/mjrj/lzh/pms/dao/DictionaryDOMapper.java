package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.AttendanceDO;
import com.mjrj.lzh.pms.entity.DictionaryDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictionaryDOMapper extends BaseMapper<DictionaryDO>{

    List<DictionaryDO> selectByColumnName(@Param("name") String name);
}