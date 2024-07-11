package com.zerozero.configuration;

import com.zerozero.core.domain.shared.Token;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Token.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    String authorizationHeader = request.getHeader("Authorization");
    String token = null;
    try {
      token = authorizationHeader.split("Bearer")[1].replace(" ", "");
    } catch (Exception e) {
      throw new AuthorizationServiceException(e.getMessage());
    }
    if (parameter.getParameterType().equals(AccessToken.class)) {
      return AccessToken.of(token);
    } else if (parameter.getParameterType().equals(RefreshToken.class)) {
      return RefreshToken.of(token);
    } else {
      throw new AuthorizationServiceException("Unsupported token type");
    }
  }
}
