package com.mjrj.lzh.pms.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;


/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.config
 * @Author: lzh
 * @CreateTime: 2020-06-01 09:59
 * @Description: ${Description}
 */
@Configuration
@Slf4j
public class JobConfig {

    public static final String JOB_KEY = "pmsSchedulerContextKey";

    //   任务调度中心
    @Bean(name = "quartzSchedulerFactory")
    public SchedulerFactoryBean quartzSchedulerFactory(@Qualifier("druidDataSource") DataSource dataSource) {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        try {
//          设置读取配置文件
            factoryBean.setQuartzProperties(PropertiesLoaderUtils.loadProperties(new ClassPathResource("quartz.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        设置quarzt容器中的任务key
        factoryBean.setApplicationContextSchedulerContextKey(JOB_KEY);
//        设置数据源
        factoryBean.setDataSource(dataSource);
//        系统启动后的10秒后启动quartz
        factoryBean.setStartupDelay(10);
//        可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factoryBean.setOverwriteExistingJobs(true);
//        设置自动启动，  默认为true
        factoryBean.setAutoStartup(true);
        return factoryBean;
    }


// jobdetai  任务
//    @Bean
//    public JobDetail teatQuartzDetail() {
//        return JobBuilder.newJob(TestQuartz.class). withIdentity("testQuartz"). storeDurably().build();
//    }
//
////    触发器
//    @Bean
//    public Trigger testQuartzTrigger() {
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule() .withIntervalInSeconds(2) //设置时间周期单位秒 每隔两秒实行一次
//         .repeatForever();
//        return TriggerBuilder.newTrigger().forJob(teatQuartzDetail()) .withIdentity("testQuartz") .withSchedule(scheduleBuilder) .build();
//    }


}