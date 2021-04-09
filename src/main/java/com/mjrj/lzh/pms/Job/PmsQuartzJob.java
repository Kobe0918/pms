package com.mjrj.lzh.pms.Job;

import com.mjrj.lzh.pms.config.JobConfig;
import com.mjrj.lzh.pms.service.JobService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.Job
 * @Author: lzh
 * @CreateTime: 2020-06-01 11:22
 * @Description: ${Description}
 */
public class PmsQuartzJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            ApplicationContext applicationContext = (ApplicationContext)jobExecutionContext.getScheduler()
                                                                 .getContext().get(JobConfig.JOB_KEY);
            JobService jobService = applicationContext.getBean(JobService.class);
            jobService.doJob(jobExecutionContext.getJobDetail().getJobDataMap());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
