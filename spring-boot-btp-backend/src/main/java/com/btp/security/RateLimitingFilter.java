package com.btp.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple sliding-window rate limiter applied only to the auth endpoints
 * that are most vulnerable to brute-force attacks (login & register).
 *
 * Limits: 10 requests per IP per 60-second window.
 * No additional library required — uses a ConcurrentHashMap with atomic updates.
 */
@Component
public class RateLimitingFilter implements Filter {

    private static final int  MAX_ATTEMPTS  = 10;
    private static final long WINDOW_MS     = 60_000L; // 1 minute

    // key = client IP, value = [attemptCount, windowStartMs]
    private final ConcurrentHashMap<String, long[]> buckets = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();
        boolean isAuthEndpoint = path.contains("/auth/login") || path.contains("/auth/register");

        if (isAuthEndpoint && isBlocked(resolveClientIp(request))) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"error\":\"Too many attempts. Please wait 60 seconds before trying again.\"}"
            );
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean isBlocked(String ip) {
        long now = System.currentTimeMillis();
        buckets.compute(ip, (key, slot) -> {
            if (slot == null || now - slot[1] >= WINDOW_MS) {
                return new long[]{1L, now};
            }
            slot[0]++;
            return slot;
        });
        long[] slot = buckets.get(ip);
        return slot != null && slot[0] > MAX_ATTEMPTS;
    }

    private String resolveClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
