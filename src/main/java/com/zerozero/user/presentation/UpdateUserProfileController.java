package com.zerozero.user.presentation;

import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.user.application.UpdateUserProfileUseCase;
import com.zerozero.user.application.UpdateUserProfileUseCase.UpdateUserProfileErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
  @PatchMapping(value = "/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UpdateUserProfileResponse> uploadProfileImage(
      @ParameterObject UpdateUserProfileRequest userProfileRequest,
      @RequestPart(required = false) @Parameter(description = "이미지 원본 파일") MultipartFile imageFile,
      @Parameter(hidden = true) AccessToken accessToken) {
    UpdateUserProfileUseCase.UpdateUserProfileResponse updateUserProfileResponse = updateUserProfileUseCase.execute(
        UpdateUserProfileUseCase.UpdateUserProfileRequest.builder()
            .nickname(userProfileRequest.getNickname())
            .imageFile(imageFile)
            .accessToken(accessToken)
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
  }
}
