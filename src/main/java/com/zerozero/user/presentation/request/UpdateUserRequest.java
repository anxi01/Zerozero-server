package com.zerozero.user.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotNull(message = "닉네임은 필수 데이터입니다.")
        @Schema(description = "닉네임", example = "제로")
        String nickname,

        @Schema(description = "사용자 프로필 이미지 URL", example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/0cbf3b99-b0ba-4148-a891-d04cb71ae236-test.png")
        String image
) {
}
