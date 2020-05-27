package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.annotation.LogAnnotation;
import com.mjrj.lzh.pms.dto.pagedto.SysLogsPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.SysLogsDO;
import com.mjrj.lzh.pms.service.SysLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-04-20 14:37
 * @Description: ${Description}
 */
@RestController
@RequestMapping("/logs")
public class SysLogsController {


    @Autowired
    private SysLogsService service;

    @PostMapping("/getLogsWithPage")
    public SysLogsPageDTO<SysLogsDO> selectByPage(@Valid @RequestBody SysLogsPageDTO dto, BindingResult result) {
        ResponseResult msg = LeaveController.getErrorMsg(result);
        if (msg.getSuccess()) {
          return   service.getLogsByPage(dto);
        }
        return  null;
    }

    @LogAnnotation(module = "删除日志")
    @DeleteMapping("/deleteLogs")
    public ResponseResult deleteLogs(@RequestBody  List<Integer> ids){
        if(!CollectionUtils.isEmpty(ids)){
            return service.deleteByIds(ids);
        }
        return new ResponseResult(false,300,"请选择数据进行操作");
    }
}
