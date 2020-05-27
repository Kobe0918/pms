package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.PmsConfigDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface PmsConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PmsConfigDO record);

    int insertSelective(PmsConfigDO record);

    PmsConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PmsConfigDO record);

    int updateByPrimaryKey(PmsConfigDO record);



    @Select("select log_cron  from pms_config  WHERE ROWNUM = 1")
    String selectCron();

    @Select("select * from pms_config where id=#{id}")
    PmsConfigDO selectConfig(@Param("id")Integer id);
}