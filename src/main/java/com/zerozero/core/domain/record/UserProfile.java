package com.zerozero.core.domain.record;

import com.zerozero.core.domain.vo.Image;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserProfile(
    @Schema(description = "닉네임", example = "제로")
    String nickname,
    @Schema(description = "프로필 사진", example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/f98da6af-d78b-43da-afb9-83ca8c762167.png")
    Image profileImage,
    @Schema(description = "제로음료 판매점 등록 순위", example = "1")
    Integer rank,
    @Schema(description = "제로음료 판매점 등록 횟수", example = "1")
    Integer storeReportCount) {
}