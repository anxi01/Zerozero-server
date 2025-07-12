package com.zerozero.review.domain.response

import com.zerozero.core.util.TimeUtil
import com.zerozero.review.domain.model.Review
import com.zerozero.review.domain.model.ZeroDrink
import com.zerozero.user.domain.model.User
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class ReviewResponse(
    @Schema(description = "리뷰 ID", example = "8e3006f4-3a16-11ef-9454-0242ac120002")
    val id: UUID,

    @Schema(description = "리뷰 내용", example = "제로 음료 판매중!")
    val content: String,

    @Schema(description = "제로 음료수 목록", example = "[\"COCA_COLA_ZERO\", \"PEPSI_ZERO\", \"SPRITE_ZERO\"]")
    val zeroDrinks: Set<ZeroDrink>,

    @Schema(description = "리뷰 작성일자, YYYY.MM.DD", example = "2024.08.27")
    val createdAt: String,

    @Schema(description = "좋아요 개수", example = "10")
    val likeCount: Int,

    @Schema(description = "사용자가 좋아요를 눌렀는지 여부", example = "true")
    val isLiked: Boolean,

    @Schema(description = "작성한 사용자 ID")
    val userId: UUID,

    @Schema(description = "닉네임", example = "제로")
    val nickname: String?,
) {
    companion object {
        fun of(review: Review, user: User, likeCount: Int, isLiked: Boolean): ReviewResponse {
            return ReviewResponse(
                id = review.id,
                content = review.content,
                zeroDrinks = review.zeroDrinks,
                createdAt = TimeUtil.toDotFormattedString(review.createdAt!!.toLocalDate()),
                likeCount = likeCount,
                isLiked = isLiked,
                userId = user.id,
                nickname = user.nickname
            )
        }
    }
}
