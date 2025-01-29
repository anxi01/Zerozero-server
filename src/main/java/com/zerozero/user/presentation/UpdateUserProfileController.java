package com.zerozero.user.presentation;

import com.zerozero.configuration.argumentresolver.LoginUser;
import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.user.application.UpdateUserProfileUseCase;
import com.zerozero.user.application.UpdateUserProfileUseCase.UpdateUserProfileErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자")
public class UpdateUserProfileController {

  private final UpdateUserProfileUseCase updateUserProfileUseCase;

  @Operation(
      summary = "프로필 수정 API",
      description = "사용자가 이미지 파일과 닉네임을 변경할 수 있습니다.",
      operationId = "/user"
  )
  @ApiErrorCode({GlobalErrorCode.class, UpdateUserProfileErrorCode.class})
  @Authorization
  @PatchMapping("/user")
  public ResponseEntity<UpdateUserProfileResponse> uploadProfileImage(
      @Valid @RequestBody UpdateUserProfileRequest request,
      @Parameter(hidden = true) @LoginUser User user) {
    UpdateUserProfileUseCase.UpdateUserProfileResponse updateUserProfileResponse = updateUserProfileUseCase.execute(
        UpdateUserProfileUseCase.UpdateUserProfileRequest.builder()
            .nickname(request.getNickname())
            .image(request.getImage())
            .user(user)
            .build());
    if (updateUserProfileResponse == null || !updateUserProfileResponse.isSuccess()) {
      Optional.ofNullable(updateUserProfileResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(UpdateUserProfileResponse.builder().build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "프로필 수정 응답")
  public static class UpdateUserProfileResponse extends BaseResponse<GlobalErrorCode> {
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "프로필 수정 요청")
  public static class UpdateUserProfileRequest implements BaseRequest {

    @NotNull(message = "닉네임은 필수 데이터입니다.")
    @Schema(description = "닉네임", example = "제로")
    private String nickname;

    @Schema(description = "판매점 업로드 이미지 URL", example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/0cbf3b99-b0ba-4148-a891-d04cb71ae236-test.png")
    private String image;
  }
}
