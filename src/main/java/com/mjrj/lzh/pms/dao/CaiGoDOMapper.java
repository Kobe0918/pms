package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.dto.pagedto.CaigoPageDTO;
import com.mjrj.lzh.pms.entity.CaiGoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CaiGoDOMapper extends BaseMapper<CaiGoDO> {
    void updateCaigoStatus(@Param("status") Byte  status , @Param("list") List<Integer> list);

    void deleteRecordByIds(@Param("list") List<Integer> caigoId);
}