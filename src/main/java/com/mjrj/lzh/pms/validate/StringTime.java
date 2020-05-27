package com.mjrj.lzh.pms.validate;

import com.mjrj.lzh.pms.validate.validateclass.StringTimeValidate;

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
 * @CreateTime: 2020-04-06 23:51
 * @Description: ${Description}
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringTimeValidate.class)//实现校验的方法
public @interface StringTime {
    String value() default ""; //默认值

    String message() default "请假时间格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
