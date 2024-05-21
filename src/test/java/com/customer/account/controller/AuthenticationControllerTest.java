package com.customer.account.controller;

import com.customer.account.AccountApplication;
import com.customer.account.user.CustomerUserDetails;
import com.customer.account.util.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Base64;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for Authtentication Controller
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AccountApplication.class)
public class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;


    private String basicAuthHeader;

    private String bearerToken;

    private String token;

    @Before
    public void setUp() {
        String username = "user";
        String password = "password";
        basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity(springSecurityFilterChain))
                .build();

        token = "test-jwt-token";
        bearerToken = "Bearer " + token;
    }

    @Test
    public void testGetToken() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password",
                Collections.singleton((GrantedAuthority) () -> null));
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(jwtUtil.generateJwtToken(Mockito.any(Authentication.class))).thenReturn(token);
        mockMvc.perform(MockMvcRequestBuilders.post("/token").header(HttpHeaders.AUTHORIZATION, basicAuthHeader).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"));
    }

    @Test
    public void testGetTokenInvalidCredentials() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/token")
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLogin() throws Exception {
        CustomerUserDetails userDetails = Mockito.mock(CustomerUserDetails.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                Collections.singleton((GrantedAuthority) () -> null));
        Mockito.when(userDetails.getUsername()).thenReturn("user");
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(HttpStatus.OK.toString()));
    }
}