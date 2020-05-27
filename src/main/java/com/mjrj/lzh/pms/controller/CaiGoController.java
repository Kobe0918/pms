package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.dao.CaiGoDOMapper;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.dto.pagedto.CaigoPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.CaiGoDO;
import com.mjrj.lzh.pms.service.BaseService;
import com.mjrj.lzh.pms.service.CaiGoService;
import com.mjrj.lzh.pms.service.impl.BaseServiceImpl;
import com.mjrj.lzh.pms.util.UserUtil;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.acl.Group;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-04-16 20:51
 * @Description: ${Description}
 */
@RestController
@RequestMapping("/caigo")
public class CaiGoController {

    @Autowired
    private CaiGoService service;

    @Autowired
    private CaiGoDOMapper mapper;

    //   采购订单
    @PostMapping("/caigo_select.do")
    public CaigoPageDTO selectCaiGoWithPage(@Valid @RequestBody CaigoPageDTO caigoPageDTO, BindingResult result) {
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if (errorMsg.getSuccess()) {
            return service.selectCaiGoWithPage(caigoPageDTO);
        }
        return null;
    }

    @PostMapping("/caigo_save.do")
    public ResponseResult saveCaiGo(@Validated(ValidationGroups.SaveCaigo.class) @RequestBody CaiGoDO caiGoDO, BindingResult result) {
        ResponseResult msg = LeaveController.getErrorMsg(result);
        if (msg.getSuccess()) {
            caiGoDO.setCaigoCreateTime(new Date());
            caiGoDO.setCaigoUserId(UserUtil.getLoginUser().getId());
            service.insertSelective(caiGoDO);
            return ResponseTool.success();
        }
        return msg;
    }



    @PostMapping("/update_caigo.do")
    public ResponseResult updateCaigo(@Validated(ValidationGroups.UpdateCaigo.class) @RequestBody CaiGoDO caiGoDO, BindingResult result ){
        ResponseResult msg = LeaveController.getErrorMsg(result);
        if (msg.getSuccess()) {
            caiGoDO.setCaigoCreateTime(new Date());
            caiGoDO.setCaigoStatus((byte)1);
            service.updateByPrimaryKeySelective(caiGoDO);
            return ResponseTool.success();
        }
        return msg;
    }

    @PostMapping("/revoke_caigo.do")
    public ResponseResult  updateStatus(@RequestBody List<Integer> caigoId){
        if(!CollectionUtils.isEmpty(caigoId)){
            return  service.revokeCaigoStatus((byte)4,caigoId);
        }
        return new ResponseResult(false,300,"请选择操作对象");
    }

    @PostMapping("/delete_caigo.do")
    public ResponseResult deleteCaigo(@RequestBody List<Integer> caigoId){
        if(caigoId == null || caigoId.size() == 0 ){
            return new ResponseResult(false,300,"请选择操作对象！");
        }
         mapper.deleteRecordByIds(caigoId);
        return ResponseTool.success();
    }

    @PostMapping("/pass_caigo.do")
    public ResponseResult  passCaigoByStatus(@RequestBody List<Integer> caigoId){
        if(!CollectionUtils.isEmpty(caigoId)){
            return  service.revokeCaigoStatus((byte)2,caigoId);
        }
        return new ResponseResult(false,300,"请选择操作对象");
    }

    @PostMapping("/unpass_caigo.do")
    public ResponseResult  nopassCaigoByStatus(@RequestBody List<Integer> caigoId){
        if(!CollectionUtils.isEmpty(caigoId)){
            return  service.revokeCaigoStatus((byte)3,caigoId);
        }
        return new ResponseResult(false,300,"请选择操作对象");
    }
}
