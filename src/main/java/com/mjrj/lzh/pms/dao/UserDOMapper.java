package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserDOMapper {
    int insert(UserDO record);

    int insertSelective(UserDO record);

    @Select("SELECT id FROM userdo WHERE telephone = #{phone}")
    String getIdByPhone(String phone);

    @Select("SELECT * FROM userdo WHERE telephone = #{phone}")
    UserDO getUserByPhone(String phone);
}