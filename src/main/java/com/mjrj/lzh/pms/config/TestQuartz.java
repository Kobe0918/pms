package com.mjrj.lzh.pms.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.config
 * @Author: lzh
 * @CreateTime: 2020-06-01 10:14
 * @Description: ${Description}
 */
@Slf4j
public class TestQuartz extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("林泽航测试开始");
    }
}
