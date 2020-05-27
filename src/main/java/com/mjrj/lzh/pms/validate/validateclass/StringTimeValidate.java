package com.mjrj.lzh.pms.validate.validateclass;

import com.github.pagehelper.util.StringUtil;
import com.mjrj.lzh.pms.validate.StringTime;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.validate.validateclass
 * @Author: lzh
 * @CreateTime: 2020-04-06 23:53
 * @Description: ${Description}
 */
public class StringTimeValidate implements ConstraintValidator<StringTime, String> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void initialize(StringTime constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(!StringUtils.isEmpty(s)){
            try {
                dateFormat.parse(s);
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
