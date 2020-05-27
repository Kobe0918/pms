package com.mjrj.lzh.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mjrj.lzh.pms.dao.BaseMapper;
import com.mjrj.lzh.pms.dao.DictionaryDOMapper;
import com.mjrj.lzh.pms.dto.pagedto.PageDTO;
import com.mjrj.lzh.pms.entity.DictionaryDO;
import com.mjrj.lzh.pms.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-04-10 04:01
 * @Description: ${Description}
 */

public class BaseServiceImpl<M extends BaseMapper<T>, T> implements BaseService<T> {

    @Autowired
    protected M mapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T record) {
        return mapper.insert(record);
    }

    @Override
    public int insertSelective(T record) {
        return mapper.insertSelective(record);
    }

    @Override
    public T selectByPrimaryKey(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(T record) { return mapper.updateByPrimaryKey(record); }



    /**
     * 反序列化
     *
     * @param json
     * @return
     */
    protected Map<String, Object> JsonUtil(String json) {
        Map <String, Object> map = null;
        try {
            if (!StringUtils.isEmpty(json)) {
                map = new HashMap<>();
                JSONObject parse = JSON.parseObject(json);
                Set<Map.Entry <String, Object>> entrySet = parse.entrySet();
                Iterator<Map.Entry <String, Object>> iterator = entrySet.iterator();
                while (iterator.hasNext()) {
                    Map.Entry <String, Object> value = iterator.next();
                    if (value.getValue() != null && !"".equals(value.getValue())) {
                        if (value.getKey().endsWith("Time")) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            map.put(value.getKey(), simpleDateFormat.parse(String.valueOf(value.getValue())));
                        } else {
                            map.put(value.getKey(), value.getValue());
                        }
                    }
                }
            }
            return map;
        } catch (ParseException e) {

            return map;
        }
    }
}
