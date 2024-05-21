package com.customer.account.service.impl;

import com.customer.account.entity.Customer;
import com.customer.account.repository.CustomerRepository;
import com.customer.account.request.RegisterRequest;
import com.customer.account.response.dto.CustomerAccountOverviewDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class RegisterCustomerServiceImplIntegrationTest {
    @Autowired
    private RegisterCustomerServiceImpl registerCustomerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Pbkdf2PasswordEncoder passwordEncoder;

    private RegisterRequest registerRequest;
    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setFirstName("Kaladin");
        registerRequest.setLastName("Stormbringer");
        registerRequest.setCity("Rotterdam");
        registerRequest.setStreet("Street");
        registerRequest.setRegion("Zuid-Holland");
        registerRequest.setDocumentId("12345");
        registerRequest.setCountryCode("NL");
        registerRequest.setDateOfBirth("12032022");
        registerRequest.setPostalCode("3333DF");

//        customer = new Customer();
//        customer.setUsername("kaladin");
//        customer.setPassword("encodedPassword");
    }

    @Test
    void testRegisterCustomer() {

        String password = registerCustomerService.registerCustomer(registerRequest);

        assertNotNull(password);
        assertTrue(password.length() > 0);

        Optional<Customer> optionalCustomer = customerRepository.findByUsername("testUser");
        assertTrue(optionalCustomer.isPresent());

        Customer customer = optionalCustomer.get();
        assertEquals("testUser", customer.getUsername());
        assertTrue(passwordEncoder.matches(password, customer.getPassword()));
    }

    @Test
    void testGetCustomerAccountOverview() {
        registerCustomerService.registerCustomer(registerRequest);
        CustomerAccountOverviewDTO overview = registerCustomerService.getCustomerAccountOverview("testUser");

        assertNotNull(overview);
        assertEquals("Kaladin Stormbringer", overview.getName());
    }

    @Test
    void testGetCustomerAccountOverview_CustomerNotFound() {
        CustomerAccountOverviewDTO overview = registerCustomerService.getCustomerAccountOverview("nonExistentUser");
        assertNull(overview);
    }
}
