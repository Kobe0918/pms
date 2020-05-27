package com.mjrj.lzh.pms.validate.validateclass;

import com.mjrj.lzh.pms.validate.Exception;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.validate
 * @Author: lzh
 * @CreateTime: 2020-04-03 13:16
 * @Description: ${Description}
 */
public class ExceptionValidate implements ConstraintValidator<Exception, Boolean> {
    private Boolean exception;
    @Override
    public void initialize(Exception constraintAnnotation) {
        exception = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Boolean aBoolean, ConstraintValidatorContext constraintValidatorContext) {
        if(aBoolean == null){
            return false;
        }
        return true;
    }
}
