package com.customer.account.util;

import com.customer.account.user.CustomerUserDetails;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {
    @Mock
    private Authentication authentication;

    @Mock
    private CustomerUserDetails customerUserDetails;

    @Mock
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        Mockito.when(authentication.getPrincipal()).thenReturn(customerUserDetails);
        Mockito.when(customerUserDetails.getUsername()).thenReturn("testUser");
    }

    @Test
    public void testGenerateJwtToken() {
        Mockito.when(authentication.getPrincipal()).thenReturn(customerUserDetails);
        Mockito.when(customerUserDetails.getUsername()).thenReturn("testUser");
        String token = jwtUtil.generateJwtToken(authentication);
        assertNotNull(token);
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        String token = jwtUtil.generateJwtToken(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(customerUserDetails);
        Mockito.when(customerUserDetails.getUsername()).thenReturn("testUser");
        String userName = jwtUtil.getUserNameFromJwtToken(token);
        assertEquals("testUser", userName);
    }

    @Test
    public void testValidateJwtToken_ValidToken() {
        String token = jwtUtil.generateJwtToken(authentication);
        boolean isValid = jwtUtil.validateJwtToken(token);
        assertTrue(isValid);
    }

    @Test
    public void testValidateJwtToken_InvalidToken() {
        String invalidToken = "invalidToken";
        Exception exception = assertThrows(MalformedJwtException.class, () -> {
            jwtUtil.validateJwtToken(invalidToken);
        });
        assertEquals("invalid", exception.getMessage());
    }

    @Test
    public void testParseJwt_ValidHeader() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        String token = jwtUtil.parseJwt(request);
        assertEquals("validToken", token);
    }

    @Test
    public void testParseJwt_InvalidHeader() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("InvalidHeader");
        String token = jwtUtil.parseJwt(request);
        assertNull(token);
    }

    @Test
    public void testParseJwt_MissingHeader() {
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String token = jwtUtil.parseJwt(request);
        assertNull(token);
    }
}