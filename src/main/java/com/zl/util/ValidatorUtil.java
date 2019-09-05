package com.zl.util;

import com.zl.entity.Status;
import com.zl.entity.User;
import com.zl.enums.ExcelType;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

/**
 * JSR303校验
 */
public class ValidatorUtil {
    private final static Validator VALIDATOR;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();
    }

    public ValidatorUtil() {
    }

    /**
     * 功能描述:
     * 〈暂未分组验证〉
     * TODO:添加group
     *
     * @param obj
     * @return : java.lang.String
     */
    public static String validation(Object obj) {
        Set<ConstraintViolation<Object>> set = null;
        set = VALIDATOR.validate(obj);
        if (set != null && set.size() > 0) {
            return getValidateErrMsg(set);
        }
        return null;
    }


    private static String getValidateErrMsg(Set<ConstraintViolation<Object>> set) {
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<Object> constraintViolation : set) {
            builder.append(constraintViolation.getMessage()).append(";");
        }
        return builder.substring(0, builder.length() - 1);
    }

    public static void main(String[] args) {
        User user = new User();
        user.setPrice(new BigDecimal(12));
        String validation = validation(user);
        System.out.println(validation);
    }


}
