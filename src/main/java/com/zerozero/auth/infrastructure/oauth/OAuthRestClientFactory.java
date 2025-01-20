package com.zerozero.auth.infrastructure.oauth;

import com.zerozero.auth.exception.AuthenticationErrorCode;
import com.zerozero.auth.infrastructure.oauth.common.Provider;
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
      default -> throw AuthenticationErrorCode.INVALID_PROVIDER.toException();
    }
  }
}
