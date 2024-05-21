package com.customer.account.authconfig;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class BucketLimitFilterTest {
    private BucketLimitFilter bucketLimitFilter;
    private FilterChain filterChain;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        bucketLimitFilter = new BucketLimitFilter();
        filterChain = Mockito.mock(FilterChain.class);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        request.setSession(session);
    }

    @Test
    void testDoFilterInternal_WithNewBucket() throws ServletException, IOException {
        bucketLimitFilter.doFilterInternal(request, response, filterChain);

        Bucket bucket = (Bucket) session.getAttribute("throttler-");
        assertNotNull(bucket, "Bucket should not be null in session");
        assertTrue(bucket.tryConsume(1), "Bucket should allow consumption");

        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WithExistingBucket_WithinLimit() throws ServletException, IOException {
        Bucket bucket = Bucket.builder()
                .addLimit(limit -> limit.capacity(20).refillGreedy(2, Duration.ofSeconds(60)))
                .build();
        session.setAttribute("throttler-", bucket);

        bucketLimitFilter.doFilterInternal(request, response, filterChain);

        assertTrue(bucket.tryConsume(1), "Bucket should allow consumption");

        verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WithExistingBucket_ExceedsLimit() throws ServletException, IOException {
        Bucket bucket = Bucket.builder()
                .addLimit(limit -> limit.capacity(1).refillGreedy(1, Duration.ofSeconds(60)))
                .build();
        session.setAttribute("throttler-", bucket);

        // Consume initial capacity
        bucket.tryConsume(1);

        bucketLimitFilter.doFilterInternal(request, response, filterChain);

        assertEquals(429, response.getStatus(), "Response status should be 429");
        assertEquals("Too many requests", response.getContentAsString(), "Response body should indicate rate limit exceeded");

        verify(filterChain, Mockito.times(0)).doFilter(request, response);
    }
}