package com.zerozero.auth.application;

import com.zerozero.auth.infrastructure.oauth.core.OAuthAccessTokenResponse;
import com.zerozero.auth.infrastructure.oauth.core.OAuthResourceResponse;

import java.net.URI;

public interface OAuthRestClient {

    URI getAuthUrl();

    OAuthAccessTokenResponse getAccessToken(String authCode);

    OAuthResourceResponse getResource(String accessToken);
}
