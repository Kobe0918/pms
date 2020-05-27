package com.mjrj.lzh.pms.validate;

import com.mjrj.lzh.pms.validate.validateclass.StatusValidate;

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
 * @CreateTime: 2020-04-03 13:02
 * @Description: ${Description}
 */


@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusValidate.class)//实现校验的方法
public @interface Status {
    byte value() default (byte)1; //默认值

    String message() default "打卡状态不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
