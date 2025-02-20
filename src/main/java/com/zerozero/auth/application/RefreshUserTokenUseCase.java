package com.zerozero.auth.application;

import com.zerozero.auth.domain.repository.RefreshTokenRepository;
import com.zerozero.auth.exception.AuthErrorType;
import com.zerozero.auth.exception.AuthException;
import com.zerozero.auth.presentation.response.TokenResponse;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.domain.repository.UserRepository;
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
public class RefreshUserTokenUseCase {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponse execute(String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            log.error("[RefreshUserTokenUseCase] Expired access token");
            throw new AuthException(AuthErrorType.EXPIRED_TOKEN);
        }

        UUID userId = jwtUtil.extractUserId(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorType.NOT_EXIST_USER));

        com.zerozero.auth.domain.model.RefreshToken alreadyExistRefreshToken = refreshTokenRepository.findById(user.getId()).orElseThrow(() -> new AuthException(AuthErrorType.NOT_EXIST_REFRESH_TOKEN));

        if (alreadyExistRefreshToken.getRefreshToken().equals(refreshToken) && jwtUtil.isTokenValid(refreshToken, user)) {
            String accessToken = jwtUtil.generateAccessToken(user);
            return TokenResponse.of(accessToken, refreshToken);
        }
        throw new AuthException(AuthErrorType.TOKEN_REFRESH_FAILED);
    }

}
