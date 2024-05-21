package com.customer.account.authconfig;

import com.customer.account.exceptions.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Authentication handler for token
 */
@Component
public class AuthEntryPointHandler implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointHandler.class);

    /**
     * Checks if the request has a valid JWT
     * @param request the servlet request
     * @param response the servlet response
     * @param authenticationException thrown when there is unauthorised access
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException {
        logger.error("Unauthorized error: {}", authenticationException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final ErrorMessage errorMessage = new ErrorMessage(Integer.toString(HttpServletResponse.SC_UNAUTHORIZED),
                authenticationException.getMessage());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorMessage);
    }
}
