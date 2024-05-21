package com.customer.account.response.mapper;

import com.customer.account.entity.Customer;
import com.customer.account.response.dto.CustomerAccountOverviewDTO;

/**
 * An object mapper for the Customer object
 */
public class CustomerAccountOverviewMapper {
    /**
     * Returns the CustomerAccountOverviewDTO for the overview display
     * @param customer the Customer Object
     * @return CustomerAccountOverviewDTO mapped from the customer
     */
    public static CustomerAccountOverviewDTO toCustomerDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerAccountOverviewDTO dto = new CustomerAccountOverviewDTO();
        dto.setName(customer.getFirstName() + " " + customer.getLastName());
        dto.setAccountType(customer.getIbanAccount().getAccountType());
        dto.setBalance(customer.getIbanAccount().getBalance().toString());
        dto.setIban(customer.getIbanAccount().getIban());
        dto.setCurrency(customer.getIbanAccount().getCurrency());
        return dto;
    }
}
