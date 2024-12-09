package net.devgrr.springdemo.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    logRequest(request);
    filterChain.doFilter(request, response);
    logResponse(response);
  }

  private void logRequest(HttpServletRequest request) {
    log.info(
        "Request: method={}, uri={}, params={}",
        request.getMethod(),
        request.getRequestURI(),
        request.getParameterMap());
  }

  private void logResponse(HttpServletResponse response) {
    log.info("Response: status={}", response.getStatus());
  }
}
