package com.customer.account.service.impl;

import com.customer.account.entity.Address;
import com.customer.account.entity.Customer;
import com.customer.account.entity.IbanAccount;
import com.customer.account.repository.CustomerRepository;
import com.customer.account.request.RegisterRequest;
import com.customer.account.response.dto.CustomerAccountOverviewDTO;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegisterCustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Pbkdf2PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterCustomerServiceImpl registerCustomerService;
    
    private RegisterRequest registerRequest;

    private Customer customer;

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

        customer = new Customer();
        customer.setUsername("kaladin");
        customer.setPassword("encodedPassword");
    }

    @Test
    void testRegisterCustomer() {
        String encodedPassword = "encodedPassword";
        Mockito.when(passwordEncoder.encode(Mockito.any(CharSequence.class))).thenReturn(encodedPassword);
        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(customer);
        String password = registerCustomerService.registerCustomer(registerRequest);

        assertNotNull(password);
        Mockito.verify(customerRepository, Mockito.times(1)).save(Mockito.any(Customer.class));

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerRepository).save(customerCaptor.capture());
        Customer savedCustomer = customerCaptor.getValue();

        assertEquals("testUser", savedCustomer.getUsername());
        assertEquals(encodedPassword, savedCustomer.getPassword());
        assertEquals("NL", savedCustomer.getAddress().getCountry());
    }

    @Test
    void testGetCustomerAccountOverview() {
        String username = "testUser";
        Customer customer = new Customer(username, "Kaladin", "Storm", "password", "123456", "1980-01-01");
        customer.setAddress(new Address("Street", "City", "Region", "12345", "NL"));
        customer.setIbanAccount(new IbanAccount("Savings", new BigDecimal("200"), "Test", Iban.random(CountryCode.NL).toString()));

        Mockito.when(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer));

        CustomerAccountOverviewDTO overview = registerCustomerService.getCustomerAccountOverview(username);

        assertNotNull(overview);
        assertEquals("Kaladin Storm", overview.getName());
    }

    @Test
    void testGetCustomerAccountOverview_CustomerNotFound() {
        String username = "nonExistentUser";

        Mockito.when(customerRepository.findByUsername(username)).thenReturn(Optional.empty());

        CustomerAccountOverviewDTO overview = registerCustomerService.getCustomerAccountOverview(username);

        assertNull(overview);
    }
}