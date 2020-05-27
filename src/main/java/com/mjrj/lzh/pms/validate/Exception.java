package com.mjrj.lzh.pms.validate;

import com.mjrj.lzh.pms.validate.validateclass.ExceptionValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.validate
 * @Author: lzh
 * @CreateTime: 2020-04-03 13:15
 * @Description: ${Description}
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExceptionValidate.class)//实现校验的方法
public @interface Exception {
    boolean value() default true; //默认值

    String message() default "是否远程打卡";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
