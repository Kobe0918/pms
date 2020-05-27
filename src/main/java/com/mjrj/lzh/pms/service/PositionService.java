package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.dto.pagedto.PositionPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.PositionDO;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-04-13 12:57
 * @Description: ${Description}
 */
public interface PositionService<T> extends BaseService<T>{
    ResponseResult selectAllBySelect2();

    ResponseResult selectPositionBySearch(String positionName);

    PositionPageDTO<PositionDO>  getPositionByPage(PositionPageDTO dto);

    ResponseResult savePosition(PositionDO positionDO);

    ResponseResult updatePosition(PositionDO positionDO);
    ResponseResult  deletePosition(Integer positionId);

}
