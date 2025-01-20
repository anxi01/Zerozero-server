package com.zerozero.auth.application;

import com.zerozero.auth.exception.AuthenticationErrorCode;
import com.zerozero.auth.infrastructure.oauth.OAuthRestClient;
import com.zerozero.auth.infrastructure.oauth.OAuthRestClientFactory;
import com.zerozero.auth.infrastructure.oauth.common.Provider;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorizeOAuthUseCase {

  private final OAuthRestClientFactory oAuthRestClientFactory;

  public URI getAuthorizeUrl(String providerName) {
    if (providerName == null || providerName.isEmpty()) {
      log.error("[AuthorizeOAuthService] Invalid provider name");
      throw AuthenticationErrorCode.NOT_EXIST_PROVIDER.toException();
    }
    Provider provider = Provider.valueOf(providerName.toUpperCase());
    OAuthRestClient oAuthRestClient = oAuthRestClientFactory.getOAuthRestClient(provider);
    return oAuthRestClient.getAuthUrl();
  }
}
