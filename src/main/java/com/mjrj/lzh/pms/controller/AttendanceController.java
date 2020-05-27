package com.mjrj.lzh.pms.controller;

import com.mjrj.lzh.pms.dto.response.ResponseCode;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.entity.AttendanceDO;
import com.mjrj.lzh.pms.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.naming.spi.DirectoryManager;
import javax.servlet.http.HttpServletRequest;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.controller
 * @Author: lzh
 * @CreateTime: 2020-03-31 14:49
 * @Description: ${Description}
 */
@RestController
@RequestMapping("/attendance")
@Slf4j
public class AttendanceController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/weather")
    public ResponseResult getWeather(){
        String apiURL = "http://wthrcdn.etouch.cn/weather_mini?city=" + "莆田";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiURL, String.class);
String data = null;
        if (200 == responseEntity.getStatusCodeValue()) {
            log.info(responseEntity.getBody());
            data = responseEntity.getBody();
        } else {
            data  = "error with code : " + responseEntity.getStatusCodeValue();
        }
        return ResponseTool.success(data);
    }

    @Autowired
    private AttendanceService service;

    /**
     * 改 4-5  0.40 添加time参数
     * @param time
     * @return
     */
    @GetMapping("/getDakaMessage")
    public ResponseResult getDakaMessage(@RequestParam(value = "time") String time){
        try {
            return service.getDakaMessage(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseTool.fail();
    }

    @GetMapping("/getDakaAddress")
    public ResponseResult getDakaAddress(@RequestParam("ip")String ip, HttpServletRequest request) throws InterruptedException {
        try {
            return service.getDakaAddress(ip);
        } catch (Exception e) {
               e.printStackTrace();
        }
        return ResponseTool.fail();
    }

    @PostMapping("/saveDakaMessage")
    public ResponseResult saveDakaMessage(@Validated  @RequestBody AttendanceDO attendance, BindingResult result){
        if (result.hasErrors()) {
            return ResponseTool.success(ResponseCode.VALIDATE_FAIL);
        }
      return   service.saveDakaMessage(attendance);
    }





}
