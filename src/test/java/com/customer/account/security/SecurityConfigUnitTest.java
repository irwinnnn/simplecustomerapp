package com.customer.account.security;

import com.customer.account.user.CustomerUserDetails;
import com.customer.account.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityConfigUnitTest {
    @LocalServerPort
    private int port;

    @Autowired
    private JwtUtil jwtUtil;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Autowired
    private TestRestTemplate template;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomerUserDetails customerUserDetails;

    @BeforeEach
    public void setup() {
        Mockito.when(authentication.getPrincipal()).thenReturn(customerUserDetails);
        Mockito.when(customerUserDetails.getUsername()).thenReturn("irwin1");
    }

    @Test
    public void givenUnauthorizedLink_shouldFailWith401() {
        ResponseEntity<String> result = template.getForEntity("/overview", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }
    
    @Test
    public void givenAuthorizedLink_shouldPass() {
        ResponseEntity<String> result = template.getForEntity("/test/hello", String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void whenAccessProtectedEndpointWithBearerToken_thenAuthorized() {
        String token = jwtUtil.generateJwtToken(authentication);
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> result = template.exchange(getBaseUrl() + "/test/private",
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}