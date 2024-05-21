package com.customer.account.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class CountryCodeValidator implements ConstraintValidator<CountryCode, String> {

    private static final Set<String> ISO_COUNTRY_CODES = new HashSet<>(Arrays.asList(
            "NL", "BE"
    ));
    @Override
    public void initialize(CountryCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        return ISO_COUNTRY_CODES.contains(value);
    }
}
