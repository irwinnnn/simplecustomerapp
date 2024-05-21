package com.customer.account.repository;

import com.customer.account.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Mock
    Customer customer;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testFindUserByUsername_customerNotFound() {
        Customer customer = customerRepository.findByUsername("TestUser").orElse(null);
        assertNull(customer, "Customer not found");
    }

    @Test
    public void testFindUserByUsername_customerFound() {
        Customer customer = customerRepository.findByUsername("irwin1").orElse(null);
        assertNotNull(customer, "Customer found");
    }

    @Test
    public void testExistsByUsername() {
        Boolean exist = customerRepository.existsByUsername("irwin1");
        assertTrue(exist);
    }
}