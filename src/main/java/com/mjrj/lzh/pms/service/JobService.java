package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.entity.JobModel;
import org.quartz.JobDataMap;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-06-01 14:32
 * @Description: ${Description}
 */
public interface JobService<T> extends  BaseService <T> {

   void  saveJob(JobModel jobModel);

   void  doJob(JobDataMap jobDataMap);
}
