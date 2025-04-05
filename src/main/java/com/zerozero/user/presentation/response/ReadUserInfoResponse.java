package com.zerozero.user.presentation.response;

import com.zerozero.image.domain.model.Image;
import com.zerozero.store.domain.response.StoreUserRankProjection;
import com.zerozero.user.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

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
    public static ReadUserInfoResponse of(User user, StoreUserRankProjection storeUserRankProjection) {
        return new ReadUserInfoResponse(
                user.getNickname(),
                Optional.ofNullable(user.getProfileImage()).map(Image::getImageUrl).orElse(null),
                storeUserRankProjection.getRank(),
                storeUserRankProjection.getStoreReportCount()
        );
    }
}
