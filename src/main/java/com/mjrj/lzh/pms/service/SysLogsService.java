package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.dto.pagedto.SysLogsPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.SysLogsDO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-03-27 16:59
 * @Description: ${Description}
 */
public interface SysLogsService<T> extends BaseService<T>{

    @Async
    void saveLog(SysLogsDO logsDO);

   SysLogsPageDTO<SysLogsDO> getLogsByPage(SysLogsPageDTO dto);

   ResponseResult deleteByIds(List<Integer> ids);

    void deleteLogs();
}
