package com.zerozero.auth.presentation.response;

import com.zerozero.user.domain.response.UserResponse;

public record LoginResponse(
        UserResponse user,

        TokenResponse token
) {
    public static LoginResponse of(UserResponse user, TokenResponse token) {
        return new LoginResponse(user, token);
    }
}
