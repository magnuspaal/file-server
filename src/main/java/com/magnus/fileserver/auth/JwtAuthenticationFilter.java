package com.magnus.fileserver.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.fileserver.controllers.exceptions.ApiErrorResponse;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {

  private final JwtService jwtService;

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
    throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.getWriter().write(new ObjectMapper().writeValueAsString(new ApiErrorResponse("Auth token not provided")));
      response.setStatus(HttpStatus.FORBIDDEN.value());
      return;
    }

    jwt = authHeader.substring(7);

    try {
      userEmail = jwtService.extractUsername(jwt);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
      response.getWriter().write(new ObjectMapper().writeValueAsString(new ApiErrorResponse(e.getMessage())));
      response.setStatus(HttpStatus.FORBIDDEN.value());
      return;
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (userEmail != null && authentication == null) {
      if (jwtService.isTokenValid(jwt)) {

        Claims claims = jwtService.extractAllClaims(jwt);

        new SimpleGrantedAuthority(claims.get("role").toString());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userEmail,
            null,
            Collections.singleton(new SimpleGrantedAuthority(claims.get("role").toString()))
        );

        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
