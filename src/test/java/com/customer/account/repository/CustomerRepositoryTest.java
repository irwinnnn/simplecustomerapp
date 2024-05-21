package com.customer.account.repository;

import com.customer.account.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class CustomerRepositoryTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    Customer customer;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testFindUserByUsername_customerNotFound() {
        Mockito.when(customerRepository.findByUsername("")).thenReturn(Optional.empty());
        Customer customer = customerRepository.findByUsername("TestUser").orElse(null);
        assertNull(customer, "Customer not found");
    }

    @Test
    public void testFindUserByUsername_customerFound() {
        Mockito.when(customerRepository.findByUsername("irwin1")).thenReturn(Optional.ofNullable(customer));
        Customer customer = customerRepository.findByUsername("irwin1").orElse(null);
        assertNotNull(customer, "Customer found");
    }
}