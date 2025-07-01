package com.zerozero.auth.application;

import com.zerozero.auth.domain.model.RefreshToken;
import com.zerozero.auth.domain.repository.RefreshTokenRepository;
import com.zerozero.auth.exception.AuthErrorType;
import com.zerozero.auth.exception.AuthException;
import com.zerozero.auth.infrastructure.oauth.core.OAuthAccessTokenResponse;
import com.zerozero.auth.infrastructure.oauth.core.OAuthResourceResponse;
import com.zerozero.auth.infrastructure.oauth.core.Provider;
import com.zerozero.auth.presentation.response.LoginResponse;
import com.zerozero.auth.presentation.response.TokenResponse;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.domain.repository.UserRepository;
import com.zerozero.user.domain.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class HandleOAuthLoginUseCase {

    private final OAuthRestClientFactory oAuthRestClientFactory;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponse execute(String code, String providerName) {
        Provider provider = Provider.valueOf(providerName.toUpperCase());
        OAuthRestClient oAuthRestClient = oAuthRestClientFactory.getOAuthRestClient(provider);

        OAuthAccessTokenResponse oAuthAccessTokenResponse = oAuthRestClient.getAccessToken(code);
        if (oAuthAccessTokenResponse == null) {
            log.error("[HandleOAuthLoginUseCase] OAuth access token is null");
            throw new AuthException(AuthErrorType.ACCESS_TOKEN_NOT_ISSUED);
        }

        OAuthResourceResponse oAuthResourceResponse = oAuthRestClient.getResource(oAuthAccessTokenResponse.accessToken());
        if (oAuthResourceResponse == null) {
            log.error("[HandleOAuthLoginUseCase] OAuth resource is null");
            throw new AuthException(AuthErrorType.NOT_EXIST_RESOURCE_RESPONSE);
        }

        String userEmail = oAuthResourceResponse.email();
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            User pendingUser = User.createPendingUser(userEmail);
            userRepository.save(pendingUser);
            return generateAndBuildResponse(pendingUser);
        } else {
            return generateAndBuildResponse(user);
        }
    }

    private LoginResponse generateAndBuildResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));
        return LoginResponse.of(UserResponse.from(user), TokenResponse.of(accessToken, refreshToken));
    }

}
