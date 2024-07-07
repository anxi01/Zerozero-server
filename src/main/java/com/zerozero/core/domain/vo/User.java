package com.zerozero.core.domain.vo;

import com.zerozero.core.domain.shared.ValueObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "사용자")
public class User extends ValueObject implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "사용자 ID", example = "8e3006f4-3a16-11ef-9454-0242ac120002")
  private UUID id;

  @Schema(description = "닉네임", example = "제로")
  private String nickname;

  @Schema(description = "이메일", example = "zero@drink.com")
  private String email;

  @Schema(description = "프로필 사진", example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/f98da6af-d78b-43da-afb9-83ca8c762167.png")
  private Image profileImage;

  public static User of(com.zerozero.core.domain.entity.User user) {
    if (user == null) {
      return null;
    }
    return User.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .profileImage(user.getProfileImage())
        .build();
  }
}
