package com.zerozero.auth.presentation.response

import com.zerozero.user.domain.response.UserResponse

@JvmRecord
data class LoginResponse(
    val user: UserResponse,
    val token: TokenResponse
) {
    companion object {
        @JvmStatic
        fun of(user: UserResponse, token: TokenResponse) = LoginResponse(user, token)
    }
}
