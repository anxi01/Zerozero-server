package com.zerozero.user.presentation.response

import com.zerozero.image.domain.model.Image
import com.zerozero.store.domain.response.StoreUserRankProjection
import com.zerozero.user.domain.model.User
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@JvmRecord
data class ReadUserInfoResponse(

    @Schema(description = "닉네임", example = "제로")
    val nickname: String?,

    @Schema(
        description = "프로필 사진",
        example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/f98da6af-d78b-43da-afb9-83ca8c762167.png",
        nullable = true
    )
    val profileImage: String?,

    @Schema(description = "제로음료 판매점 등록 순위", example = "1")
    val rank: Int,

    @field:Schema(description = "제로음료 판매점 등록 횟수", example = "1")
    val storeReportCount: Int
) {
    companion object {
        @JvmStatic
        fun of(user: User, storeUserRankProjection: StoreUserRankProjection): ReadUserInfoResponse {
            return ReadUserInfoResponse(
                user.nickname,
                user.profileImage?.imageUrl,
                storeUserRankProjection.rank,
                storeUserRankProjection.storeReportCount
            )
        }
    }
}
