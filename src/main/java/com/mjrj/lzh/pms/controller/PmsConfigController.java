package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.dao.PmsConfigDOMapper;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.PmsConfigDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-04-16 22:52
 * @Description: ${Description}
 */
@RestController
@Slf4j
@RequestMapping("/config")
public class PmsConfigController {
    @Autowired
    private PmsConfigDOMapper mapper;

    @GetMapping("/getCaiGoCheckUserSelect2")
    public ResponseResult getCaiGoCheckUserSelect2(@RequestParam(value = "search")String userName){
        if(StringUtils.isEmpty(userName)){
            return new ResponseResult(false,300,"输入正确的选项");
        }
        return null;
    }

    @GetMapping("/get")
    public ResponseResult get(){
        PmsConfigDO configDO = (PmsConfigDO)mapper.selectByPrimaryKey(1);
        log.info(configDO.toString());
        return ResponseTool.success(configDO);
    }

    @PostMapping("/editConfig")
    public ResponseResult editConfig(@RequestBody PmsConfigDO pmsConfigDO){
        mapper.updateByPrimaryKeySelective(pmsConfigDO);
        return ResponseTool.success();
    }

}
