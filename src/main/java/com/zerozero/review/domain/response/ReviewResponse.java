package com.zerozero.review.domain.response;

import com.zerozero.core.util.TimeUtil;
import com.zerozero.review.domain.model.Review;
import com.zerozero.review.domain.model.ZeroDrink;
import com.zerozero.user.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;
import java.util.UUID;

public record ReviewResponse(

        @Schema(description = "리뷰 ID", example = "8e3006f4-3a16-11ef-9454-0242ac120002")
        UUID id,

        @Schema(description = "리뷰 내용", example = "제로 음료 판매중!")
        String content,

        @Schema(description = "제로 음료수 목록", example = "[\"COCA_COLA_ZERO\", \"PEPSI_ZERO\", \"SPRITE_ZERO\"]")
        Set<ZeroDrink> zeroDrinks,

        @Schema(description = "리뷰 작성일자, YYYY.MM.DD", example = "2024.08.27")
        String createdAt,

        @Schema(description = "좋아요 개수", example = "10")
        int likeCount,

        @Schema(description = "사용자가 좋아요를 눌렀는지 여부", example = "true")
        boolean isLiked,

        @Schema(description = "작성한 사용자 ID")
        UUID userId,

        @Schema(description = "닉네임", example = "제로")
        String nickname
) {
    public static ReviewResponse of(Review review, User user, int likeCount, boolean isLiked) {
        return new ReviewResponse(
                review.getId(),
                review.getContent(),
                review.getZeroDrinks(),
                TimeUtil.toDotFormattedString(review.getCreatedAt().toLocalDate()),
                likeCount,
                isLiked,
                user.getId(),
                user.getNickname()
        );
    }
}
