package com.customer.account.service;

import com.customer.account.request.RegisterRequest;
import com.customer.account.response.dto.CustomerAccountOverviewDTO;

public interface RegisterCustomerService {
    /**
     * Method to create and register the user
     * @param registerRequest The registration request form
     * @return The generated password
     */
    String registerCustomer(final RegisterRequest registerRequest);

    /**
     * Retrieves the customer and returns the account overview
     * @param username Username used for login
     * @return CustomerAccountOverviewDTO used as a success response
     */
    CustomerAccountOverviewDTO getCustomerAccountOverview(final String username);
}
