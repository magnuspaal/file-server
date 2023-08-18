package com.magnus.fileserver.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.fileserver.config.AppProperties;
import com.magnus.fileserver.controllers.exceptions.ApiErrorResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter implements Filter {

  Logger logger = LoggerFactory.getLogger(ApiKeyAuthenticationFilter.class);
  private final AppProperties appProperties;

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    final String apiKey = request.getHeader("X-Api-Key");

    if (apiKey == null || !apiKey.equals(appProperties.getApiKey())) {
      try {
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ApiErrorResponse("Invalid Api Key")));
      } catch (IOException e) {
        logger.error("Failed to write Invalid Api Key response", e);
        return;
      }
      response.setStatus(HttpStatus.FORBIDDEN.value());
      return;
    }

    Authentication authentication = new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}

