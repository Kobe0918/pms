package com.mjrj.lzh.pms.config;

import com.mjrj.lzh.pms.dao.PmsConfigDOMapper;
import com.mjrj.lzh.pms.service.SysLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.config
 * @Author: lzh
 * @CreateTime: 2020-04-20 23:36
 * @Description: ${Description}
 */
@Configuration
@Slf4j
public class DynamicScheduleTask implements SchedulingConfigurer {
    @Autowired
    private PmsConfigDOMapper configMapper;
    @Autowired
    private SysLogsService logsService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            logsService.deleteByPrimaryKey(1);
                },
                triggerContext -> {
                    String cron = configMapper.selectCron();
                    if (StringUtils.isEmpty(cron)) {
                        cron = "0/20 * * * * ?";  //设置默认更新时间
                    }
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });
    }



}
