package com.zerozero.auth.application;

import com.zerozero.auth.exception.AuthErrorType;
import com.zerozero.auth.exception.AuthException;
import com.zerozero.auth.infrastructure.oauth.core.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorizeOAuthUseCase {

    private final OAuthRestClientFactory oAuthRestClientFactory;

    public URI getAuthorizeUrl(String providerName) {
        if (providerName == null || providerName.isEmpty()) {
            log.error("[AuthorizeOAuthService] Invalid provider name");
            throw new AuthException(AuthErrorType.NOT_EXIST_PROVIDER);
        }
        Provider provider = Provider.valueOf(providerName.toUpperCase());
        OAuthRestClient oAuthRestClient = oAuthRestClientFactory.getOAuthRestClient(provider);
        return oAuthRestClient.getAuthUrl();
    }
}
