package com.zerozero.configuration.argumentresolver;

import com.zerozero.auth.exception.AuthErrorType;
import com.zerozero.auth.exception.AuthException;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.domain.model.UserStatus;
import com.zerozero.user.domain.repository.UserRepository;
import com.zerozero.user.exception.UserErrorType;
import com.zerozero.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String AUTHORIZATION_BEARER_PREFIX = "Bearer";

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorizationHeader = webRequest.getHeader(AUTHORIZATION_HEADER);
        String token = extractToken(authorizationHeader);
        UUID userId = jwtUtil.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorType.NOT_EXIST_USER));
        if (user.getUserStatus() != UserStatus.COMPLETED) {
            throw new UserException(UserErrorType.NOT_COMPLETED_MEMBER);
        }
        return user;
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new AuthException(AuthErrorType.NOT_EXIST_HEADER);
        }
        try {
            return authorizationHeader.split(AUTHORIZATION_BEARER_PREFIX)[1].replace(" ", "");
        } catch (Exception e) {
            throw new AuthException(AuthErrorType.NOT_EXIST_TOKEN);
        }
    }
}
