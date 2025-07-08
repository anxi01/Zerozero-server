package com.zerozero.auth.presentation.response

import io.swagger.v3.oas.annotations.media.Schema

@JvmRecord
data class TokenResponse(
    @Schema(description = "액세스 토큰")
    val accessToken: String,

    @Schema(description = "리프레시 토큰")
    val refreshToken: String
) {
    companion object {
        @JvmStatic
        fun of(accessToken: String, refreshToken: String) = TokenResponse(accessToken, refreshToken)
    }
}
