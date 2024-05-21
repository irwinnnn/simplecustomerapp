package com.customer.account.validator;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DateFormatValidatorTest {
    private DateFormatValidator dateFormatValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        dateFormatValidator = new DateFormatValidator();
        DateFormat dateFormat = new DateFormat() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return DateFormat.class;
            }

            @Override
            public String message() {
                return null;
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String pattern() {
                return "yyyy-MM-dd";
            }
        };
        dateFormatValidator.initialize(dateFormat);
    }

    @Test
    void testIsValid_ValidDate() {
        assertTrue(dateFormatValidator.isValid("2024-05-19", constraintValidatorContext));
    }

    @Test
    void testIsValid_InvalidDate_Format() {
        assertFalse(dateFormatValidator.isValid("19-05-2024", constraintValidatorContext));
    }

    @Test
    void testIsValid_InvalidDate_NonExistentDate() {
        assertFalse(dateFormatValidator.isValid("2024-02-30", constraintValidatorContext));
    }

    @Test
    void testIsValid_NullDate() {
        assertFalse(dateFormatValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void testIsValid_EmptyDate() {
        assertFalse(dateFormatValidator.isValid("", constraintValidatorContext));
    }
}