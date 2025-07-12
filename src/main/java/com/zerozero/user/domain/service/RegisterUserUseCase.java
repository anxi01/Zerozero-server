package com.zerozero.user.domain.service;

import com.zerozero.auth.presentation.request.RegisterRequest;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.domain.repository.UserRepository;
import com.zerozero.user.domain.response.UserResponse;
import com.zerozero.user.exception.UserErrorType;
import com.zerozero.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class RegisterUserUseCase {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    public UserResponse execute(RegisterRequest registerRequest, String accessToken) {
        UUID userId = jwtUtil.extractUserId(accessToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorType.NOT_EXIST_USER));

        if (user.isRegistered()) {
            log.error("[RegisterUserUseCase] User with id {} is already registered", userId);
            throw new UserException(UserErrorType.ALREADY_REGISTERED_USER);
        }

        user.completePendingUser(registerRequest.nickname());
        return UserResponse.from(user);
    }

}
