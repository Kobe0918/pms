package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.activemq.Producer;
import com.mjrj.lzh.pms.dao.SysUserDOMapper;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.dto.ResponseDTO;
import com.mjrj.lzh.pms.dto.StatusEnum;
import com.mjrj.lzh.pms.dto.UserDTO;
import com.mjrj.lzh.pms.dto.pagedto.UserPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.SysUserDO;
import com.mjrj.lzh.pms.service.SysUserService;
import com.mjrj.lzh.pms.springsecurity.response.JsonResult;
import com.mjrj.lzh.pms.util.UserUtil;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-03-27 19:44
 * @Description: ${Description}
 */
@RequestMapping("/user")
@RestController
@Slf4j
public class SysUserController {
    @Autowired
    private Producer producer;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserDOMapper mapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private  SysUserDOMapper sysUserDOMapper;


    @GetMapping("/current")
    public JsonResult getCurrentUser(@RequestParam("token") String token){
        System.out.println(token);
       return  sysUserService.validateUser();
    }

    @GetMapping("/getSelect2ByUserName")
    public ResponseResult getUserSelect2(@RequestParam(value = "search")String userName){
          if(StringUtils.isEmpty(userName)){
              return new ResponseResult(false,300,"输入正确的选项");
          }
          return sysUserService.getUserSelect2(userName);
    }

    @GetMapping("/getCaiGoCheckUserSelect2")
    public ResponseResult getCaiGoCheckUserSelect2(){
        return sysUserService.selectCaiGoCheckUserId();
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @GetMapping ("/sendMessage")
    public ResponseDTO sendMessage(@RequestParam("phone") String phone){
        // 1.先判断用户有没乱点，重复去注册发送短信或者执行忘记操作（redis查找手机号码）
        log.info(phone+" : phone");
        int result = mapper.getCountByPhone(phone);
        if(0 == result){
            StatusEnum sendSms = producer.sendMessage("sendSms", phone);
            return new ResponseDTO(sendSms);
        }
        return  new ResponseDTO(StatusEnum.EXIT_USER);
    }

    @PostMapping("/register")
    public ResponseResult register(@Validated(ValidationGroups.Register.class) @RequestBody SysUserDO user , BindingResult result){
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if (!errorMsg.getSuccess()) {
            return errorMsg;
        }
        return sysUserService.register(user);
    }

    @PostMapping("/forgot")
    public ResponseResult forgotPassword(@Validated(ValidationGroups.Forgot.class) @RequestBody SysUserDO user , BindingResult result){
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if (!errorMsg.getSuccess()) {
            return errorMsg;
        }
        return  sysUserService.forgotPassword(user);
    }

    @GetMapping("/getUp")
    public ResponseResult getUp(@RequestParam("id")Integer userId){
        return  sysUserService.selectBossForLeave(userId);
    }

    @GetMapping("/getPersonnel")
    public ResponseResult getPersonnel(){
        return sysUserService.selectPersonnelLeave();
    }


    @GetMapping("/getNameAndDeptName")
    public ResponseResult getNameAndDeptName(){
        CurrentUserDTO dto = UserUtil.getLoginUser();
        dto.setPassword(null);
        return ResponseTool
                .success(dto);
    }

    @GetMapping("/getMyMessage")
    public ResponseResult getUserBySelf(){
        SysUserDO user  = (SysUserDO)sysUserService.selectByPrimaryKey(UserUtil.getLoginUser().getId());
        user.setPassword(null);
        return ResponseTool
                .success(user);
    }

    @PostMapping("/updateUserMessage")
    public ResponseResult updateUserMessage(@RequestParam("file") MultipartFile file, @Validated(ValidationGroups.UpdateUser.class) UserDTO dto, BindingResult result, HttpServletRequest request){
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if(errorMsg.getSuccess()){
                return sysUserService.updateUser(file,dto,request);
        }
        return errorMsg;
    }

    @PostMapping("/updateUserByAdmin")
    public ResponseResult updateUserByAdmin(@RequestParam("file") MultipartFile file, @Validated(ValidationGroups.UpdateUser.class) UserDTO dto, BindingResult result, HttpServletRequest request){
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if(errorMsg.getSuccess()){
            return sysUserService.updateUser(file,dto,request);
        }
        return errorMsg;
    }


    @PostMapping("/select_userPage.do")
    public UserPageDTO selectByPage(@Valid @RequestBody UserPageDTO pageDTO,BindingResult result){
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if(errorMsg.getSuccess()){
            return sysUserService.selectWithPage(pageDTO);
        }
        return null;
    }

    @PostMapping("/updatePassword")
    public ResponseResult updatePassword(@Validated(ValidationGroups.Forgot.class) @RequestBody SysUserDO user , BindingResult result){
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if (!errorMsg.getSuccess()) {
            return errorMsg;
        }
        return  sysUserService.updatePassword(user);
    }


    @PostMapping("/update_userEnabled.do")
    public ResponseResult updateUserEnable(@RequestParam Integer id, Boolean status){
        if(StringUtils.isEmpty(id)){
            return new ResponseResult(false,300,"请选中一条记录进行操作");
        }
        mapper.updateEnabledByPrimaryKey(id,status);
        return ResponseTool.success();
    }


    @PutMapping("/update_passByAdmin.do")
    public ResponseResult updatePasswordByAdmin(@RequestParam Integer id,  String password,String checkPassword){
        if(!StringUtils.isEmpty(password) && !StringUtils.isEmpty(checkPassword)){
            if(password.equals(checkPassword)){
                SysUserDO user = new SysUserDO();
                user.setId(id);
                user.setUpdateUser(UserUtil.getLoginUser().getId());
                user.setUpdateTime(new Date());
                user.setPassword(passwordEncoder.encode(password));
                mapper.updateByPrimaryKeySelective(user);
                return  ResponseTool.success();
            }
            return new ResponseResult(false,300,"两次密码不一致");
        }
        return new ResponseResult(false,300,"密码和确认密码均不可为空");
    }

    @PostMapping("/saveUserByAdmin")
    public ResponseResult saveUserByAdmin(@RequestParam("file") MultipartFile file,@Validated(ValidationGroups.AddUser.class) UserDTO user,BindingResult result,HttpServletRequest request){
        ResponseResult errorMsg = LeaveController.getErrorMsg(result);
        if(errorMsg.getSuccess()){
            return sysUserService.addUser(file,user,request);
        }
        return errorMsg;
    }
}
