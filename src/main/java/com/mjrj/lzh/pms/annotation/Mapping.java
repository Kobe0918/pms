package com.mjrj.lzh.pms.annotation;

import java.lang.annotation.*;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.annotation
 * @Author: lzh
 * @CreateTime: 2020-05-27 19:47
 * @Description: ${Description}
 */
@Documented
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Mapping {
    public String key();

    public int length() default -1;

    public String rex() default "";

    public boolean delNull() default false;
}