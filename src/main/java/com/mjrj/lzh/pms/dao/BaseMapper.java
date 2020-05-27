package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.dto.pagedto.PageDTO;
import com.mjrj.lzh.pms.dto.pagedto.PositionPageDTO;
import com.mjrj.lzh.pms.entity.AttendanceDO;
import com.mjrj.lzh.pms.entity.PositionDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dao
 * @Author: lzh
 * @CreateTime: 2020-03-22 21:14
 * @Description: ${Description}
 */
@Repository
public interface BaseMapper<T> {

    int deleteByPrimaryKey(Integer id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

    List<T> selectByPage();

    int updateStatus(@Param("id") List<Integer> id);

    List<T>  selectByPage(PageDTO dto);

}
