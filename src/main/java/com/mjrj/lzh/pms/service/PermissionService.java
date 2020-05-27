package com.mjrj.lzh.pms.service;

import com.alibaba.fastjson.JSONArray;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.SysPermissionDO;

import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-04-16 19:47
 * @Description: ${Description}
 */
public interface PermissionService<T> extends BaseService<T>  {
    JSONArray selectAll();

    ResponseResult getAll();
}
