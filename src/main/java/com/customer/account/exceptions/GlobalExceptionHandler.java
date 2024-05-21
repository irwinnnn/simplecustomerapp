package com.customer.account.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Exception catcher for invalid registration fields
     * @param ex MethodArgumentNotValidException thrown for invalid field value
     * @return A map of field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * A generic catch all exception
     * @param ex Generic exception for catch all
     * @return The ErrorMessage object
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception ex) {
        logger.error("Generic exception", ex);
        return new ResponseEntity<>(new ErrorMessage(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "Error has occurred."), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception for invalid username/password
     * @param ex BadCredentialsException thrown for invalid credentials
     * @return The ErrorMessage object
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> test(BadCredentialsException ex) {
        logger.error("Bad credentials", ex);
        return new ResponseEntity<>(new ErrorMessage(Integer.toString(HttpStatus.UNAUTHORIZED.value()),
                "Invalid username/password"), HttpStatus.UNAUTHORIZED);
    }

}
