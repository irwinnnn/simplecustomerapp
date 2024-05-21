package com.customer.account.controller;

import com.customer.account.AccountApplication;
import com.customer.account.repository.CustomerRepository;
import com.customer.account.request.LoginRequest;
import com.customer.account.request.RegisterRequest;
import com.customer.account.response.dto.CustomerAccountOverviewDTO;
import com.customer.account.service.RegisterCustomerService;
import com.customer.account.service.impl.CustomerDetailsServiceImpl;
import com.customer.account.user.CustomerUserDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the CustomerController
 */

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AccountApplication.class)
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private CustomerDetailsServiceImpl customerService;

    @MockBean
    private RegisterCustomerService registerCustomerService;

    @MockBean
    private Pbkdf2PasswordEncoder passwordEncoder;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private CustomerUserDetails customerUserDetails;
    private String bearerToken;
    private String jsonRequest = "{\"username\":\"username\"," +
            "\"firstName\":\"John\",\"lastName\":\"Doe\"," +
            "\"documentId\":\"123456\",\"dateOfBirth\":\"12031990\"," +
            "\"street\":\"123 Main St\",\"city\":\"Anytown\"," +
            "\"region\":\"Anystate\",\"postalCode\":\"12345\",\"countryCode\":\"NL\"}";

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setDocumentId("123456");
        registerRequest.setDateOfBirth("12031990");
        registerRequest.setStreet("123 Main St");
        registerRequest.setCity("Anytown");
        registerRequest.setRegion("Anystate");
        registerRequest.setPostalCode("12345");
        registerRequest.setCountryCode("NL");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("password");

        customerUserDetails = Mockito.mock(CustomerUserDetails.class);
        Mockito.when(customerUserDetails.getUsername()).thenReturn("username");

        String token = "test-jwt-token";
        bearerToken = "Bearer " + token;

        Mockito.when(customerService.getCustomerRepository()).thenReturn(customerRepository);
    }

    @Test
    public void testRegisterUser() throws Exception {
        Mockito.when(customerService.getCustomerRepository().existsByUsername(registerRequest.getUsername())).thenReturn(false);
        Mockito.when(registerCustomerService.registerCustomer(any(RegisterRequest.class))).thenReturn("generatedPW");

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("username", registerRequest.getUsername());
        expectedResponse.put("password", "generatedPW");

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.password").value("generatedPW"));
    }

    @Test
    public void testRegisterUserUsernameTaken() throws Exception {
        Mockito.when(customerService.getCustomerRepository().existsByUsername(registerRequest.getUsername())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Error: Username is already taken!"));
    }

    @Test
    public void testGetOverview() throws Exception {
        CustomerUserDetails userDetails = Mockito.mock(CustomerUserDetails.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                Collections.singleton((GrantedAuthority) () -> null));
        Mockito.when(userDetails.getUsername()).thenReturn("user");
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(registerCustomerService.getCustomerAccountOverview(any(String.class)))
                .thenReturn(new CustomerAccountOverviewDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/overview")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }
}