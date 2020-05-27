package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.activemq.Producer;
import com.mjrj.lzh.pms.dto.ResponseDTO;
import com.mjrj.lzh.pms.entity.DeptDO;
import com.mjrj.lzh.pms.entity.PositionDO;
import com.mjrj.lzh.pms.entity.UserDO;
import com.mjrj.lzh.pms.service.LoginService;
import com.mjrj.lzh.pms.dto.StatusEnum;
import com.mjrj.lzh.pms.validate.validateclass.ValidationGroups;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-03-03 16:03
 * @Description: ${Description}
 */
@RestController
@RequestMapping("/")
@Slf4j
public class LoginController {

    @Autowired
    private Producer producer;

    @Autowired
    private LoginService loginService;


//    @PostMapping ("/login")
//    public ResponseDTO login(@Validated(ValidationGroups.Register.class) @RequestBody UserDO user,  BindingResult result,
//                        HttpServletRequest request, ServletResponse response) {
//        System.out.println("enter");
//        if (result.hasErrors()) {
//            return new ResponseDTO(StatusEnum.VALIDATE_FAIL);
//        }
//        return new ResponseDTO(loginService.login(user));
//    }








}
