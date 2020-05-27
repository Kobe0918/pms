package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.entity.DictionaryDO;
import com.mjrj.lzh.pms.service.DictionaryService;
import com.mjrj.lzh.pms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-04-10 04:12
 * @Description: ${Description}
 */
@RestController
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/getDicByColumnName")
    public List<DictionaryDO> getDicByColumnName(@RequestParam("columnName")String n){
        if(!StringUtils.isEmpty(n)){
            return dictionaryService.selectByColumnName(n);
        }
        return null;
    }
}
