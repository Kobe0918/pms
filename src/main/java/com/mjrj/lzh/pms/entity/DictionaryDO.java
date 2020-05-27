package com.mjrj.lzh.pms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryDO implements Serializable {

    private static final long serialVersionUID = 2134120487308387828L;

    private Integer dictionaryId;

    private String columnName;

    private Byte columnCode;

    private String columnValue;

    private Boolean columnStatus;


}