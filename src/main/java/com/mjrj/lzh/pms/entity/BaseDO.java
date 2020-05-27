package com.mjrj.lzh.pms.entity;

import lombok.Data;

import java.io.Serializable;
 @Data
public abstract class BaseDO<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 2054813493011812469L;


    private ID id;

}
