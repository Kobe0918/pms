package com.mjrj.lzh.pms.validate.validateclass;

import com.mjrj.lzh.pms.validate.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.validate
 * @Author: lzh
 * @CreateTime: 2020-04-03 13:04
 * @Description: ${Description}
 */
public class StatusValidate implements ConstraintValidator<Status,Byte> {
    private Byte status ;
    @Override
    public void initialize(Status constraintAnnotation) {
        status =  constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Byte aByte, ConstraintValidatorContext constraintValidatorContext) {
        if(aByte == null){
            return false;
        }
        return true;
    }
}
