package com.mjrj.lzh.pms.service.impl;

import com.mjrj.lzh.pms.Job.PmsQuartzJob;
import com.mjrj.lzh.pms.dao.JobMapper;
import com.mjrj.lzh.pms.entity.JobModel;
import com.mjrj.lzh.pms.service.JobService;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-06-01 14:31
 * @Description: ${Description}
 */
@Service
public class JobServiceImpl extends BaseServiceImpl<JobMapper,JobModel> implements JobService<JobModel>  {

    private static final String JOB_DATA_KEY = "JOB_DATA_KEY";
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void saveJob(JobModel jobModel) {
      checkJob(jobModel);
      String jobName = jobModel.getJobName();
      String description = jobModel.getDescription();
//      配置jobdetail
        JobKey jobKey = JobKey.jobKey(jobName);
        JobDetail jobDetail = JobBuilder.newJob(PmsQuartzJob.class).storeDurably()
                .withDescription(description).withIdentity(jobKey).build();
//        quartz 启动时会读取JobDataMap集合中的任务
        jobDetail.getJobDataMap().put(JOB_DATA_KEY,jobModel);



    }

    private void checkJob(JobModel jobModel){
//        判断是否已经存在该任务
        JobModel model = mapper.getByName(jobModel.getJobName());
        if(model != null){
            throw new IllegalArgumentException(jobModel.getJobName()+"已存在");
        }
        jobModel.setIsSysJob(false);
//       判断是否存在该beanName
        String springBeanName = jobModel.getSpringBeanName();
        boolean flag = applicationContext.containsBean(springBeanName);
        if(!flag) {
            throw new IllegalArgumentException("bean：" + springBeanName + "不存在，bean名如userServiceImpl,首字母小写");
        }
//        校验是否存在method
        Object object = applicationContext.getBean(springBeanName);
        Class <?> clazz = object.getClass();
//        是否为切面方法
        if (AopUtils.isAopProxy(object)) {
            clazz = clazz.getSuperclass();
        }
        String methodName = jobModel.getMethodName();
//        类的所有方法
        Method[] methods = clazz.getDeclaredMethods();
        Set<String> names = new HashSet<>();
//        遍历类的所有方法，无参的加入
        Arrays.asList(methods).forEach(m -> {
            Class<?>[] classes = m.getParameterTypes();
            if (classes.length == 0) {
                names.add(m.getName());
            }
        });

//        判断该bean是否有无参方法
        if (names.size() == 0) {
            throw new IllegalArgumentException("该bean没有无参方法");
        }

//        判断该类中是否有前台传来的job调用方法。
        if (!names.contains(methodName)) {
            throw new IllegalArgumentException("未找到无参方法" + methodName + ",该bean所有方法名为：" + names);
        }
    }

    @Override
    public void doJob(JobDataMap jobDataMap) {

    }
}
