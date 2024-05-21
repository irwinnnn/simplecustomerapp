package com.customer.account.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CountryCodeValidatorTest {
    private CountryCodeValidator countryCodeValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        countryCodeValidator = new CountryCodeValidator();
        countryCodeValidator.initialize(null); // No initialization needed for this validator
    }

    @Test
    void testIsValid_ValidCountryCode_NL() {
        assertTrue(countryCodeValidator.isValid("NL", constraintValidatorContext));
    }

    @Test
    void testIsValid_ValidCountryCode_BE() {
        assertTrue(countryCodeValidator.isValid("BE", constraintValidatorContext));
    }

    @Test
    void testIsValid_InvalidCountryCode() {
        assertFalse(countryCodeValidator.isValid("US", constraintValidatorContext));
    }

    @Test
    void testIsValid_NullValue() {
        assertFalse(countryCodeValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void testIsValid_EmptyValue() {
        assertFalse(countryCodeValidator.isValid("", constraintValidatorContext));
    }

    @Test
    void testIsValid_LowerCaseCountryCode() {
        assertFalse(countryCodeValidator.isValid("nl", constraintValidatorContext));
    }

    @Test
    void testIsValid_NumericValue() {
        assertFalse(countryCodeValidator.isValid("123", constraintValidatorContext));
    }
}