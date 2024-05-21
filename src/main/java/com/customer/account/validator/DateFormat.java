package com.customer.account.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateFormatValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormat {
    String message() default "Invalid date format. Expected format is dd-MM-yyyy";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    String pattern();
}
