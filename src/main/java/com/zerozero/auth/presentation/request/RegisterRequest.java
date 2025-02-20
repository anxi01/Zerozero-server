package com.zerozero.auth.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotNull(message = "닉네임은 필수 데이터입니다.")
        @Schema(description = "닉네임", example = "제로")
        String nickname
) {
}
