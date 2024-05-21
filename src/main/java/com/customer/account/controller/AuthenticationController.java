package com.customer.account.controller;

import com.customer.account.exceptions.ErrorMessage;
import com.customer.account.user.CustomerUserDetails;
import com.customer.account.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling token and login request
 */
@RestController
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Generates the bearer token after authentication
     * @param authorization The basic auth credentials
     * @return The bearer token
     */
    @Operation(summary = "Generates the bearer token after authentication of the basic auth\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the bearer token",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "401", description = "Invalid username/password",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})})
    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestHeader("Authorization") String authorization) {
        if (authorization != null && authorization.startsWith("Basic ")) {
            String base64Credentials = authorization.substring(6);
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            logger.info("generating token for {}" , username);
            if (authentication.isAuthenticated()) {
                Map<String, String> response = new HashMap<>();
                response.put("token", jwtUtil.generateJwtToken(authentication));
                return ResponseEntity.ok(response);
            }
        }
        throw new RuntimeException("Invalid Basic Authentication credentials");
    }

    /**
     * Authenticates the customer using the bearer token
     * @return A message of the login result
     */
    @Operation(summary = "Generates an Ok response upon success")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok response as success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})})
    @PostMapping("/login")
    public ResponseEntity<?> login() {
        CustomerUserDetails customerUserDetails = (CustomerUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        logger.info("Customer login: " +customerUserDetails.getUsername());
        return ResponseEntity.ok(HttpStatus.OK.toString());
    }


}
