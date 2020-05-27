package com.mjrj.lzh.pms.dto.pagedto;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.pagedto
 * @Author: lzh
 * @CreateTime: 2020-04-16 21:02
 * @Description: ${Description}
 */
@Data
public class CaigoPageDTO<CaigoDo> extends PageDTO<CaigoDo> implements Serializable {

    private Integer caigoUserId;


    private Byte caigoStatus;

    private String caigoName;
    private String role;
    private String checkUser;
    private Integer caigoSpiuserId;

}
