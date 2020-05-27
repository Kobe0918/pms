package com.mjrj.lzh.pms.component;

import com.mjrj.lzh.pms.annotation.LogAnnotation;
import com.mjrj.lzh.pms.entity.SysLogsDO;
import com.mjrj.lzh.pms.service.SysLogsService;
import com.mjrj.lzh.pms.util.UserUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Date;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.component
 * @Author: lzh
 * @CreateTime: 2020-04-19 15:35
 * @Description: ${Description}
 */
@Aspect
@Component
public class LogAdvice {

    @Autowired
    private SysLogsService logsService;


    @Around(value = "@annotation(com.mjrj.lzh.pms.annotation.LogAnnotation)")
    public Object logSave(ProceedingJoinPoint joinPoint) throws Throwable  {
        SysLogsDO logsDO = new SysLogsDO();
        logsDO.setUserId(UserUtil.getLoginUser().getId());
        logsDO.setCreatetime(new Date());
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();

        LogAnnotation logAnnotation = signature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
        String modual = logAnnotation.module();
        if(StringUtils.isEmpty(modual)){
            throw new RuntimeException("没有指定日志module");
        }
        logsDO.setModule(modual);

        try{
            Object proceed = joinPoint.proceed();
            logsDO.setFlag((byte)1);
            return proceed;
        }catch(Exception e){
            logsDO.setFlag((byte)0);
            logsDO.setRemark(e.getMessage());
            throw e;
        }finally {
            if(logsDO.getUserId() != null){
                logsService.saveLog(logsDO);
            }
        }
    }

}
