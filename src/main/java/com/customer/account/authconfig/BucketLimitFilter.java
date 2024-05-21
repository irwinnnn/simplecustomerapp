package com.customer.account.authconfig;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

/**
 * Bucket limit filter that handles request limiting
 */
@Component
public class BucketLimitFilter  extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(BucketLimitFilter.class);
    /**
     * Total bucket capacity of 2, refills every 1 second
     */
    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(2).refillGreedy(2, Duration.ofSeconds(1)))
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final HttpSession session = request.getSession(true);
        final String bucketName = "throttler-";
        logger.info("Bucket name: {}", bucketName);
        Bucket bucket = (Bucket) session.getAttribute(bucketName);
        if (bucket == null) {
            bucket = createNewBucket();
            session.setAttribute(bucketName, bucket);
        }

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setContentType("text/plain");
            response.setStatus(429);
            response.getWriter().append("Too many requests");
        }
    }
}
