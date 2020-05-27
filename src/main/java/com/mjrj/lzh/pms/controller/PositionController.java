package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.dto.pagedto.PositionPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.DeptDO;
import com.mjrj.lzh.pms.entity.PositionDO;
import com.mjrj.lzh.pms.service.PositionService;
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
 * @CreateTime: 2020-04-12 21:44
 * @Description: ${Description}
 */
@RestController
@RequestMapping("/position")
@Slf4j
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping("/getPositionBySelect2")
    public ResponseResult getPositionBySelect2(){
        return positionService.selectAllBySelect2();
    }



    @GetMapping("/getPositionBySearch")
    public ResponseResult getPositionBySearch(@RequestParam(value = "search")String positionName){
        log.info(positionName + " : getPoinamew");
        if(StringUtils.isEmpty(positionName)){
            return new ResponseResult(false,300,"输入正确的选项");
        }
        return positionService.selectPositionBySearch(positionName);
    }


    @PreAuthorize("hasAuthority('position:select')")
    @PostMapping("/select_position.do")
    public PositionPageDTO<PositionDO> getPositionWithPage(@Valid @RequestBody PositionPageDTO dto, BindingResult result) {
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if (errorMsg.getSuccess()) {
            return positionService.getPositionByPage(dto);
        }
        return null;
    }


    @PreAuthorize("hasAuthority('position:delete')")
    @PostMapping("/delete_position.do")
    public ResponseResult updateDeptStatus(@RequestParam("positionId") Integer positionId, @RequestParam("positionStatus") Boolean positionStatus) {
        if (StringUtils.isEmpty(positionId) || positionStatus == null) {
            return new ResponseResult(false, 300, "请选择一条记录");
        }
        PositionDO positionDO = new PositionDO(positionId, positionStatus, UserUtil.getLoginUser().getId(), new Date());
        positionService.updateByPrimaryKeySelective(positionDO);
        return ResponseTool.success();
    }

    @PreAuthorize("hasAuthority('position:save')")
    @PostMapping("/save_position.do")
    public ResponseResult savePosition(@Validated(ValidationGroups.SavePosition.class) @RequestBody PositionDO positionDO, BindingResult result) {
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if (errorMsg.getSuccess()) {
            log.info(positionDO.toString());
            positionService.savePosition(positionDO);
        }
        return errorMsg;
    }

    @PreAuthorize("hasAuthority('position:update')")
    @PostMapping("/update_position.do")
    public ResponseResult updatePosition(@Validated(ValidationGroups.UpdatePosition.class) @RequestBody PositionDO positionDO, BindingResult result) {
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if (errorMsg.getSuccess()) {
            return positionService.updatePosition(positionDO);
        }
        return errorMsg;
    }

    @PreAuthorize("hasAuthority('position:delete:forever')")
    @PostMapping("/delete_forever.do")
    public ResponseResult deletePosition(@RequestParam("id") Integer positionId) {
        if (StringUtils.isEmpty(positionId)) {
            return new ResponseResult(false, 300, "选择一条记录极限操作");
        }
        return positionService.deletePosition(positionId);
    }

}
