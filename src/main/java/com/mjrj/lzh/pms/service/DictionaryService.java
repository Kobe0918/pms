package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.entity.DictionaryDO;

import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-04-12 22:20
 * @Description: ${Description}
 */
public interface DictionaryService<T> extends  BaseService <T>{
    List<DictionaryDO> selectByColumnName(String name);
}
