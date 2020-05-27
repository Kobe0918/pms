package com.mjrj.lzh.pms.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mjrj.lzh.pms.dao.SysPermissionDOMapper;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.SysPermissionDO;
import com.mjrj.lzh.pms.service.PermissionService;
import com.mjrj.lzh.pms.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-04-16 19:47
 * @Description: ${Description}
 */
@Service
@Slf4j
public class PermissionServiceImpl  extends BaseServiceImpl<SysPermissionDOMapper, SysPermissionDO> implements PermissionService<SysPermissionDO> {

       @Override
        public JSONArray selectAll(){
           List <SysPermissionDO> permissionsAll = mapper.selectAll();
           JSONArray array = new JSONArray();
           setPermissionsTree(0, permissionsAll, array);
           return array;
       }


    /**
     * 菜单树
     *
     * @param pId
     * @param permissionsAll
     * @param array
     */
    private void setPermissionsTree(Integer pId, List<SysPermissionDO> permissionsAll, JSONArray array) {
        for (SysPermissionDO per : permissionsAll) {
            if (per.getParentId().equals(pId)) {
                String string = JSONObject.toJSONString(per);
                JSONObject parent = (JSONObject) JSONObject.parse(string);
                array.add(parent);

                if (permissionsAll.stream().filter(p -> p.getParentId().equals(per.getId())).findAny() != null) {
                    JSONArray child = new JSONArray();
                    parent.put("child", child);
                    setPermissionsTree( per.getId(), permissionsAll, child);
                }
            }
        }
    }

    @Override
    public ResponseResult getAll(){
        List <SysPermissionDO> all = mapper.getAll();
        return ResponseTool.success(all);
    }
}
