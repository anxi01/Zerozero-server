package com.zerozero.configuration.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerozero.core.exception.error.ErrorCode;
import com.zerozero.core.presentation.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (JwtException e) {
      sendErrorResponse(response, ErrorCode.EXPIRED_JWT_EXCEPTION);
    }
  }

  private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setStatus(errorCode.getHttpStatus());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");
    ObjectMapper objectMapper = new ObjectMapper();
    ErrorResponse errorResponse = ErrorResponse.from(errorCode);
    Map<String, ErrorResponse> result = new HashMap<>();
    result.put("result", errorResponse);
    response.getWriter().write(objectMapper.writeValueAsString(result));
  }
}
