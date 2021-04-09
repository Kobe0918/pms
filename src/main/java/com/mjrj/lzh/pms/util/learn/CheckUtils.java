package com.mjrj.lzh.pms.util.learn;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class CheckUtils {

    public static void notNull(Object obj, String msg){
        if(obj == null){
            throw new RuntimeException(msg);
        }
    }

    public static void notEmpty(Collection<?> list){
        if(CollectionUtils.isEmpty(list)){
            throw new RuntimeException("集合不为空！");
        }
    }

    public static void notEmpty(Collection<?> list, String msg){
        if(CollectionUtils.isEmpty(list)){
            throw new RuntimeException(msg);
        }
    }

    public static void hasLength(String text, String msg) {
        if (!StringUtils.hasLength(text)) {
            throw new RuntimeException(msg);
        }
    }
}
