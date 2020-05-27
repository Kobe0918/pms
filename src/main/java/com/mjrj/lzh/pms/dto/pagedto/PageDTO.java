package com.mjrj.lzh.pms.dto.pagedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto
 * @Author: lzh
 * @CreateTime: 2020-04-07 23:33
 * @Description: ${Description}
 */
@Data
public class PageDTO<T> implements Serializable {

    private static final long serialVersionUID = 6618705248333203687L;

    @NotNull(message = "draw 为无效请求")
    private Integer draw;
    @NotNull(message = "起始页码无效")
    private Integer start;
    @NotNull(message = "分页长度无效")
    private Integer length;

    private Integer recordsTotal;

    private Integer recordsFiltered;

    private Integer totalNum;

    private List<T> data;


}
