package com.example.clientmanagement.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger("API-LOGGER");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();
        Throwable failure = null;
        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } catch (Throwable ex) {
            failure = ex;
            throw ex;
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
            String responseBody = new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);

            log.info(">>> REQUEST  id={} method={} uri={} query={} client={} body={}",
                    resolveRequestId(request), request.getMethod(), request.getRequestURI(),
                    request.getQueryString(), request.getRemoteAddr(), abbreviate(requestBody));

            log.info("<<< RESPONSE id={} status={} durationMs={} body={}{}",
                    resolveRequestId(request), wrappedResponse.getStatus(), elapsed,
                    abbreviate(responseBody), failure == null ? "" : " error=" + failure.getClass().getSimpleName());

            wrappedResponse.copyBodyToResponse();
        }
    }

    private String resolveRequestId(HttpServletRequest request) {
        Object id = request.getAttribute("requestId");
        return id == null ? "-" : id.toString();
    }

    private String abbreviate(String value) {
        if (value == null || value.isEmpty()) {
            return "<empty>";
        }
        return value.length() > 4000 ? value.substring(0, 4000) + "...(truncated)" : value;
    }
}
