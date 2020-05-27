package com.mjrj.lzh.pms.validate.validateclass;

import com.mjrj.lzh.pms.validate.Image;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.validate.validateclass
 * @Author: lzh
 * @CreateTime: 2020-04-07 00:14
 * @Description: ${Description}
 */
public class ImageValidate implements ConstraintValidator<Image, String[]> {
    String[] style = {"jpg", "png","gif"};
    @Override
    public boolean isValid(String[] s, ConstraintValidatorContext constraintValidatorContext) {
        if(s != null && s.length > 0){
            int i = s.length;
            for (String value : s) {
                String fileSuffix = value.substring(value.lastIndexOf(".")+1);
                System.out.println(fileSuffix+":后最");
                if(!Arrays.asList(style).contains(fileSuffix)){
                    return false;
                }
            }
        }
        return true;
    }
}
