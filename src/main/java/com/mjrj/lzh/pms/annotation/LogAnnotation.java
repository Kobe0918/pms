package com.mjrj.lzh.pms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.annotation
 * @Author: lzh
 * @CreateTime: 2020-04-19 15:34
 * @Description: ${Description}
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAnnotation {
    String module() default "";
}
