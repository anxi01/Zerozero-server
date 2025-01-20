package com.zerozero.auth.infrastructure.oauth;

import com.zerozero.auth.infrastructure.oauth.common.OAuthAccessTokenResponse;
import com.zerozero.auth.infrastructure.oauth.common.OAuthResourceResponse;
import java.net.URI;

public interface OAuthRestClient {

  URI getAuthUrl();

  OAuthAccessTokenResponse getAccessToken(String authCode);

  OAuthResourceResponse getResource(String accessToken);
}
