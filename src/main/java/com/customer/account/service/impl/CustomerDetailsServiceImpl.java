package com.customer.account.service.impl;

import com.customer.account.entity.Customer;
import com.customer.account.repository.CustomerRepository;
import com.customer.account.user.CustomerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for Account creation
 */
@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    @Autowired
    public CustomerDetailsServiceImpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer user = getCustomerRepository().findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer Not Found with username: " + username));
        return new CustomerUserDetails(user.getId(), user.getUsername(), user.getPassword());
    }
}
