package com.zerozero.user.domain.response

import com.zerozero.user.domain.model.User
import com.zerozero.user.domain.model.UserStatus
import com.zerozero.user.exception.UserErrorType
import com.zerozero.user.exception.UserException
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class UserResponse(
    @Schema(description = "사용자 ID", example = "8e3006f4-3a16-11ef-9454-0242ac120002")
    val id: UUID,

    @Schema(description = "닉네임", example = "제로")
    val nickname: String,

    @Schema(description = "이메일", example = "zero@drink.com")
    val email: String,

    @Schema(
        description = "프로필 사진",
        example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/f98da6af-d78b-43da-afb9-83ca8c762167.png"
    )
    val profileImage: String?,

    @Schema(
        description = "사용자 가입 여부 (COMPLETED, PENDING)",
        example = "COMPLETED"
    )
    val userStatus: UserStatus
) {

    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id,
                nickname = user.nickname ?: throw UserException(UserErrorType.NOT_EXIST_USER),
                email = user.email,
                profileImage = user.profileImage?.imageUrl,
                userStatus = user.userStatus
            )
        }
    }
}
