package com.zerozero.auth.application

import com.zerozero.auth.exception.AuthErrorType
import com.zerozero.auth.exception.AuthException
import com.zerozero.auth.infrastructure.oauth.core.Provider
import com.zerozero.auth.infrastructure.oauth.kakao.KakaoOAuthRestClient
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
class OAuthRestClientFactory(
    private val kakaoOAuthRestClient: KakaoOAuthRestClient
) {

    fun getOAuthRestClient(provider: Provider): OAuthRestClient {
        when (provider) {
            Provider.KAKAO -> return kakaoOAuthRestClient
            else -> throw AuthException(AuthErrorType.INVALID_PROVIDER)
        }
    }
}
