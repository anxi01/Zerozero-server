package com.zerozero.user.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ReadUserInfoResponse(
        @Schema(description = "닉네임", example = "제로")
        String nickname,
        @Schema(description = "프로필 사진", example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/f98da6af-d78b-43da-afb9-83ca8c762167.png")
        String profileImage,
        @Schema(description = "제로음료 판매점 등록 순위", example = "1")
        int rank,
        @Schema(description = "제로음료 판매점 등록 횟수", example = "1")
        int storeReportCount
) {
}
