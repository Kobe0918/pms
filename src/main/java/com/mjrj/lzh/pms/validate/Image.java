package com.mjrj.lzh.pms.validate;

import com.mjrj.lzh.pms.validate.validateclass.ImageValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.validate
 * @Author: lzh
 * @CreateTime: 2020-04-07 00:09
 * @Description: ${Description}
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidate.class)//实现校验的方法
public @interface Image {


    String[] value() default {};    //默认值

    String message() default "文件类型仅支持png,jpg,gif";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
