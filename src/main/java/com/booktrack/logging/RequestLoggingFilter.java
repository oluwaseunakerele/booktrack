package com.booktrack.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Logs every HTTP request with a correlation id (rid), status and duration.
 * Runs once per request near the start of the filter chain.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RequestLoggingFilter extends OncePerRequestFilter {

  private static final Logger log =
      LoggerFactory.getLogger(RequestLoggingFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain)
      throws ServletException, IOException {

    String rid = UUID.randomUUID().toString().substring(0, 8);
    MDC.put("rid", rid); // put rid into the logging context so it appears in all lines

    long t0 = System.currentTimeMillis();
    try {
      log.info(">>> {} {}", request.getMethod(), request.getRequestURI());
      chain.doFilter(request, response);
    } finally {
      long took = System.currentTimeMillis() - t0;
      log.info("<<< {} {} [{}] {}ms",
               request.getMethod(), request.getRequestURI(),
               response.getStatus(), took);
      MDC.clear();
    }
  }
}
