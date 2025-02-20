package com.zerozero.auth.application;

import com.zerozero.auth.exception.AuthErrorType;
import com.zerozero.auth.exception.AuthException;
import com.zerozero.auth.infrastructure.oauth.core.Provider;
import com.zerozero.auth.infrastructure.oauth.kakao.KakaoOAuthRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthRestClientFactory {

    private final KakaoOAuthRestClient kakaoOAuthRestClient;

    public OAuthRestClient getOAuthRestClient(Provider provider) {
        switch (provider) {
            case KAKAO -> {
                return kakaoOAuthRestClient;
            }
            default -> throw new AuthException(AuthErrorType.INVALID_PROVIDER);
        }
    }
}
