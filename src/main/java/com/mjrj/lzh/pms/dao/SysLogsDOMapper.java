package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.SysLogsDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysLogsDOMapper extends BaseMapper<SysLogsDO>{

    void  deleteByIds(List<Integer> list);
}