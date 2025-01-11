package com.zerozero.configuration.argumentresolver;

import com.zerozero.auth.error.AuthenticationErrorCode;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String AUTHORIZATION_BEARER_PREFIX = "Bearer";

    private final JwtUtil jwtUtil;

    private final UserJPARepository userJPARepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorizationHeader = webRequest.getHeader(AUTHORIZATION_HEADER);
        String token = extractToken(authorizationHeader);
        String email = jwtUtil.extractUsername(token);
        User user = userJPARepository.findByEmail(email);
        return Optional.ofNullable(user).orElseThrow(AuthenticationErrorCode.NOT_FOUND_MEMBER::toException);
    }

    private String extractToken(String authorizationHeader) throws AuthenticationException {
        if (authorizationHeader == null) {
            throw AuthenticationErrorCode.NOT_EXIST_HEADER.toException();
        }
        try {
            return authorizationHeader.split(AUTHORIZATION_BEARER_PREFIX)[1].replace(" ", "");
        } catch (Exception e) {
            throw AuthenticationErrorCode.NOT_EXIST_TOKEN.toException();
        }
    }
}
