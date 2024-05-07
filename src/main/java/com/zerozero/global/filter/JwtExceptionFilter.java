package com.zerozero.global.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (JwtException e) {
      sendErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
    }
  }

  private void sendErrorResponse(HttpStatus status, HttpServletResponse response, Exception e) throws IOException {
    response.setStatus(status.value());
    response.setContentType("application/json");
    response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
  }
}
