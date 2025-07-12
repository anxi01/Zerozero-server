package com.zerozero.auth.presentation.response

import com.zerozero.user.domain.response.UserResponse

data class LoginResponse(
    val user: UserResponse,
    val token: TokenResponse
) {
    companion object {
        fun of(user: UserResponse, token: TokenResponse) = LoginResponse(user, token)
    }
}
