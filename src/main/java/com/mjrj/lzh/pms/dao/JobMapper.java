package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.entity.JobModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dao
 * @Author: lzh
 * @CreateTime: 2020-06-01 14:49
 * @Description: ${Description}
 */
@Repository
public interface JobMapper extends BaseMapper<JobModel>{

    @Select(value = "select * from t_job t where t.jobName = #{jobName}")
    public JobModel getByName(@Param("jobName") String jobName);
}
