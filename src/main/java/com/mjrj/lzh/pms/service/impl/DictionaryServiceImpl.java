package com.mjrj.lzh.pms.service.impl;

import com.mjrj.lzh.pms.dao.DictionaryDOMapper;
import com.mjrj.lzh.pms.entity.DictionaryDO;
import com.mjrj.lzh.pms.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-04-12 22:20
 * @Description: ${Description}
 */
@Slf4j
@Service
public class DictionaryServiceImpl extends BaseServiceImpl<DictionaryDOMapper,DictionaryDO> implements DictionaryService<DictionaryDO> {

    @Override
    public List<DictionaryDO> selectByColumnName(String name) {
        return mapper.selectByColumnName(name);
    }
}
