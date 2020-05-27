package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.dto.pagedto.PageDTO;
import com.mjrj.lzh.pms.entity.DictionaryDO;

import java.util.List;

/**
 * @BelongsProject: transport-system
 * @BelongsPackage: com.fteplus.transport.service
 * @Author: 小林子
 * @CreateTime: 2019-10-08 12:49
 * @Description: ${Description}
 */
public interface BaseService<T> {
    int deleteByPrimaryKey(Integer id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);



}
