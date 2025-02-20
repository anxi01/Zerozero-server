package com.zerozero.auth.infrastructure.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoResourceResponse(
        Long id,

        @JsonProperty("kakao_account")
        Response kakaoAccount
) {
    public record Response(
            String email,
            Profile profile
    ) {
        public record Profile(
                String nickname
        ) {
        }
    }
}
