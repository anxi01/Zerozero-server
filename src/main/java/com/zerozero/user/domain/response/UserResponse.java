package com.zerozero.user.domain.response;

import com.zerozero.image.domain.model.Image;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.domain.model.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;
import java.util.UUID;

public record UserResponse(

        @Schema(description = "사용자 ID", example = "8e3006f4-3a16-11ef-9454-0242ac120002")
        UUID id,

        @Schema(description = "닉네임", example = "제로")
        String nickname,

        @Schema(description = "이메일", example = "zero@drink.com")
        String email,

        @Schema(description = "프로필 사진", example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/f98da6af-d78b-43da-afb9-83ca8c762167.png")
        String profileImage,

        @Schema(description = "사용자 가입 여부 (COMPLETED, PENDING)", example = "COMPLETED")
        UserStatus userStatus
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                Optional.ofNullable(user.getProfileImage()).map(Image::getImageUrl).orElse(null),
                user.getUserStatus()
        );
    }
}
