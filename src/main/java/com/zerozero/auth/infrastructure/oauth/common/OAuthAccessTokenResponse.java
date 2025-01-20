package com.zerozero.auth.infrastructure.oauth.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthAccessTokenResponse(
    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("expires_in")
    long expiresIn,

    @JsonProperty("refresh_token")
    String refreshToken,

    @JsonProperty("token_type")
    String tokenType
) {
}
