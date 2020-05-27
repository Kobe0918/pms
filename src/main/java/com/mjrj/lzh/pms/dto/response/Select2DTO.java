package com.mjrj.lzh.pms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.dto.response
 * @Author: lzh
 * @CreateTime: 2020-04-13 23:53
 * @Description: ${Description}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Select2DTO implements Serializable {
    private static final long serialVersionUID = 7308004761568017591L;

    private Integer id;
    private String text;


}
