package com.mjrj.lzh.pms.entity;

import lombok.Data;

@Data
public class JobModel extends BaseDO<Long> {

	private static final long serialVersionUID = -2458935535811207209L;

	private String jobName;

	private String description;

	private String cron;

	private String springBeanName;

	private String methodName;

	private Boolean isSysJob;

	private int status;


}
