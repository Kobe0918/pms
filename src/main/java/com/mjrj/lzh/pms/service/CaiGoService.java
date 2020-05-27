package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.dto.pagedto.CaigoPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;

import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-04-16 20:58
 * @Description: ${Description}
 */
public interface CaiGoService<T> extends BaseService<T> {
    CaigoPageDTO selectCaiGoWithPage(CaigoPageDTO dto);

    ResponseResult revokeCaigoStatus(Byte status,List<Integer> ids);
}
