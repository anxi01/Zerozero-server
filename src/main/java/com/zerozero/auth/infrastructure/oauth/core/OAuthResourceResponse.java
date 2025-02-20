package com.zerozero.auth.infrastructure.oauth.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record OAuthResourceResponse(
        String id,

        String email,

        @JsonProperty("verified_email")
        boolean verifiedEmail
) {
}
