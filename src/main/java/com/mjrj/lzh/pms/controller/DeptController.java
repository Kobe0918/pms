package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.annotation.LogAnnotation;
import com.mjrj.lzh.pms.dto.pagedto.DeptPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.DeptDO;
import com.mjrj.lzh.pms.service.DeptService;
import com.mjrj.lzh.pms.util.UserUtil;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-04-11 20:17
 * @Description: ${Description}
 */
@RestController
@RequestMapping("/dept")
@Slf4j
public class DeptController {
    @Autowired
    private DeptService deptService;


    @PreAuthorize("hasAuthority('dept:select')")
    @PostMapping("/select_dept.do")
    public DeptPageDTO <DeptDO> getDeptWithPage(@Valid @RequestBody DeptPageDTO dto, BindingResult result) {
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if (errorMsg.getSuccess()) {
            return deptService.getDeptByPage(dto);
        }
        return null;
    }

    @LogAnnotation(module = "修改部门状态")
    @PreAuthorize("hasAuthority('dept:delete')")
    @PostMapping("/delete_dept.do")
    public ResponseResult updateDeptStatus(@RequestParam("deptId") Integer deptId, @RequestParam("deptStatus") Boolean deptStatus) {
        if (StringUtils.isEmpty(deptId)) {
            return new ResponseResult(false, 300, "请选择一条记录");
        }
        log.info(deptId + ":deptid");
        DeptDO deptDO = new DeptDO(deptId, deptStatus, UserUtil.getLoginUser().getId(), new Date());
        deptService.updateByPrimaryKeySelective(deptDO);
        return ResponseTool.success();
    }

    @LogAnnotation(module = "添加部门")
    @PreAuthorize("hasAuthority('dept:save')")
    @PostMapping("/save_dept.do")
    public ResponseResult saveDept(@Validated(ValidationGroups.SaveDept.class) @RequestBody DeptDO deptDO, BindingResult result) {
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if (errorMsg.getSuccess()) {
            deptService.saveDept(deptDO);
        }
        return errorMsg;
    }

    @LogAnnotation(module = "修改部门信息")
    @PreAuthorize("hasAuthority('dept:update')")
    @PostMapping("/update_dept.do")
    public ResponseResult updateDept(@Validated(ValidationGroups.UpdateDept.class) @RequestBody DeptDO deptDO, BindingResult result) {
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if (errorMsg.getSuccess()) {
           return deptService.updateDept(deptDO);
        }
        return errorMsg;
    }

    @LogAnnotation(module = "禁用部门")
    @PreAuthorize("hasAuthority('dept:delete:forever')")
    @PostMapping("/delete_forever.do")
    public ResponseResult deleteDept(@RequestParam("id") Integer deptId) {
        if (StringUtils.isEmpty(deptId)) {
            return new ResponseResult(false, 300, "选择一条记录极限操作");
        }

        return deptService.deleteDept(deptId);
    }

    @GetMapping("/getDeptAll")
    public ResponseResult getDeptAll(@RequestParam(value = "search")String deptName){
        log.info(deptName);
        if(StringUtils.isEmpty(deptName)){
            return new ResponseResult(false,300,"输入正确的选项");
        }
        return deptService.getDeptByName(deptName);
    }



}
