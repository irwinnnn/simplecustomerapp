package com.customer.account.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatValidator implements ConstraintValidator<DateFormat, String>
{
    private String pattern;

    @Override
    public void initialize(DateFormat constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String dateText, ConstraintValidatorContext constraintValidatorContext) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            final Date date = sdf.parse(dateText);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
