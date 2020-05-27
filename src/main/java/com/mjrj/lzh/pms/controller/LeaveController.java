package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.dto.LeaveDTO;
import com.mjrj.lzh.pms.dto.pagedto.LeavePageDTO;
import com.mjrj.lzh.pms.dto.pagedto.PageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.LeaveDO;
import com.mjrj.lzh.pms.service.LeaveService;
import com.mjrj.lzh.pms.util.UploadFileUtil;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-04-05 23:03
 * @Description: ${Description}
 */
@RestController
@RequestMapping("/leave")
@Slf4j
public class LeaveController {

@Autowired
private UploadFileUtil uploadFileUtil;

@Autowired
private LeaveService leaveService;




    /**
     * 个人查看请假记录  分页
     * @param pageDTO
     * @return
     */
//    @PreAuthorize("hasAnyAuthority('leave:select','sys:leave:select')")
    @PostMapping("/select_leave.do")
    public LeavePageDTO<LeaveDO> selectLeaveByUser(@Valid @RequestBody LeavePageDTO pageDTO,BindingResult result){
        log.info(pageDTO.toString());
        ResponseResult errorMsg = getErrorMsg(result);
        if(errorMsg.getSuccess()){
            return  leaveService.getLeaveByUserWithPage(pageDTO);
        }
        return new LeavePageDTO <>(errorMsg.getErrorCode()+":"+errorMsg.getErrorMsg());

    }

    /**
     * 个人修改请假记录
     * @param dto
     * @param result
     * @return
     * @throws ParseException
     */
//    @PreAuthorize("hasAuthority('leave:update')")
    @PostMapping("/update_leave.do")
    public ResponseResult updateLeaveBySelf(@Validated(ValidationGroups.UpdateLeave.class) @RequestBody LeaveDTO dto, BindingResult result) {
        ResponseResult response = getErrorMsg(result);
        if(response.getSuccess()){
            return  leaveService.updateLeaveByUserSelf(dto);
        }
        return response;
    }

    /**
     * 个人撤回请假记录
     * @param leaveId
     * @return
     */
//    @PreAuthorize("hasAuthority('leave:revoke')")
    @PostMapping("/revoke_leave.do")
    public ResponseResult revokeLeaveBySelf(@RequestBody List<Integer> leaveId){
        if(leaveId != null && leaveId.size() != 0){
            return leaveService.revokeLeaveBySelf(leaveId);
        }
        return new ResponseResult(false,300,"请选择撤销的假条");
    }

    /**
     * 保存文件
     * @param file
     * @param request
     * @return
     */
//    @PreAuthorize("hasAuthority('leave:saveFile')")
    @PostMapping("/save_file.do")
    public ResponseResult saveFile( @RequestParam("imgUrl") MultipartFile file, HttpServletRequest request  ){
        try {
            log.info("save_file");
           return  uploadFileUtil.uploadFile(file,request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseTool.fail();
    }

    /**
     * 新增模式下 删除
     * @param img
     * @param request
     * @return
     */
//    @PreAuthorize("hasAuthority('leave:deleteFile')")
    @PostMapping("/delete_file.do")
    public ResponseResult deleteFile(@RequestBody List<String> img, HttpServletRequest request){
        if(img != null && img.size() == 0 ){
            return new ResponseResult(false,300,"请选择文件");
        }
       return uploadFileUtil.deleteFile(img,request);
    }


    /**
     * 修改模式下  删除
     * @param  {leaveId:,path}
     * @param request
     * @return
     */
//    @PreAuthorize("hasAuthority('leave:updateFile')")
    @PostMapping("/delete_file2.do")
    public ResponseResult deleteFile2( @RequestParam("key") Integer leaveId,@RequestParam("path") String path, HttpServletRequest request){  //@Validated(ValidationGroups.UpdateFile.class) LeaveDTO dto ,BindingResult result
        if(StringUtils.isEmpty(leaveId) || StringUtils.isEmpty(path)){
            return new ResponseResult(false,300,"传输出问题");
        }
        LeaveDTO dto =new LeaveDTO();
        dto.setLeaveId(leaveId);
        dto.setPath(path);
        return leaveService.deleteFileWhenUpdate(dto,request);
    }

    /**
     * 保存请假条
     * @param leave
     * @param result
     * @return
     */
//    @PreAuthorize("hasAuthority('leave:save')")
    @PostMapping("/save_leave.do")
    public ResponseResult saveLeave(@Validated(ValidationGroups.SaveLeave.class)@RequestBody  LeaveDTO leave, BindingResult result)  {
        ResponseResult response = getErrorMsg(result);
        if(response.getSuccess()){
//            需要通过postman校验开始时间需要早与结束时间
//            if(leave.getBeginTime()){
//
//            }
            return  leaveService.saveLeaveBySelf(leave);
        }
        return response;
    }


    public  static  ResponseResult getErrorMsg( BindingResult result){
        if(result.hasErrors()){
            List<FieldError> list = result.getFieldErrors();
            String msg = null;
            for (FieldError fieldError : list) {
                msg = fieldError.getDefaultMessage();
                break;
            }
            return new ResponseResult(false,300,msg);
        }
        return new ResponseResult(true);
    }


    @PreAuthorize("hasAuthority ('sys:leave:edit')")
    @PostMapping("/leave_pass.do")
    public ResponseResult leavePass(@RequestBody List<Integer> ids){
        if(ids != null && ids.size() != 0){
            return leaveService.passLeave(ids);
        }
        return new ResponseResult(false,300,"请选择撤销的假条");
    }


    @PreAuthorize("hasAuthority ('sys:leave:edit')")
    @PostMapping("/leave_unPass.do")
    public ResponseResult leaveUnPass(@RequestBody List<Integer> ids){
        if(ids != null && ids.size() != 0){
            return leaveService.UnpassLeave(ids);
        }
        return new ResponseResult(false,300,"请选择撤销的假条");
    }
}
