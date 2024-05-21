package com.customer.account.controller;

import com.customer.account.exceptions.ErrorMessage;
import com.customer.account.request.RegisterRequest;
import com.customer.account.response.dto.CustomerAccountOverviewDTO;
import com.customer.account.service.RegisterCustomerService;
import com.customer.account.service.impl.CustomerDetailsServiceImpl;
import com.customer.account.user.CustomerUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * The customer Controller that handles registration and overview
 */
@RestController
public class CustomerController {

    private final CustomerDetailsServiceImpl customerService;
    private final RegisterCustomerService registerCustomerService;

    @Autowired
    public CustomerController(CustomerDetailsServiceImpl customerService, RegisterCustomerService registerCustomerService) {
        this.customerService = customerService;
        this.registerCustomerService = registerCustomerService;
    }

    /**
     * Registers the customer with the RegisterRequest request body and returns a generated password
     * @param registerRequest The registerRequest form object
     */
    @Operation(summary = "Registers the customer and generates a password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upon successful registration, password is generated for the customer",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})})
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        if (customerService.getCustomerRepository().existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("Error: Username is already taken!");
        }
        String generatedPW = registerCustomerService.registerCustomer(registerRequest);
        Map<String, String> response = new HashMap<>();
        response.put("username", registerRequest.getUsername());
        response.put("password", generatedPW);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets the customer's account overview
     * @return Returns the CustomerAccountOverviewDTO for the overview
     * @throws AccountNotFoundException
     */
    @Operation(summary = "Requires the authenticated bearer token, returns the customer overview")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the customer account overview details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerAccountOverviewDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})})
    @GetMapping("/overview")
    public ResponseEntity<?> getOverview() throws AccountNotFoundException {
        CustomerUserDetails customerUserDetails = (CustomerUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if(customerUserDetails == null){
            throw new AccountNotFoundException();
        }
        return ResponseEntity.ok(registerCustomerService.getCustomerAccountOverview(customerUserDetails.getUsername()));
    }
}
