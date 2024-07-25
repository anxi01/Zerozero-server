package com.zerozero.user.presentation;

import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.user.application.UploadProfileImageUseCase;
import com.zerozero.user.application.UploadProfileImageUseCase.UploadProfileImageErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자")
public class UploadProfileImageController {

  private final UploadProfileImageUseCase uploadProfileImageUseCase;

  @Operation(
      summary = "프로필 사진 등록 API",
      description = "사용자가 이미지 파일을 통해 프로필 사진을 등록합니다.",
      operationId = "/user/image"
  )
  @ApiErrorCode({GlobalErrorCode.class, UploadProfileImageErrorCode.class})
  @PostMapping(value = "/user/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UploadProfileImageResponse> uploadProfileImage(
      @RequestPart @Parameter(description = "이미지 원본 파일") MultipartFile imageFile,
      @Parameter(hidden = true) AccessToken accessToken) {
    UploadProfileImageUseCase.UploadProfileImageResponse uploadProfileImageResponse = uploadProfileImageUseCase.execute(
        UploadProfileImageUseCase.UploadProfileImageRequest.builder()
            .imageFile(imageFile)
            .accessToken(accessToken)
            .build());
    if (uploadProfileImageResponse == null || !uploadProfileImageResponse.isSuccess()) {
      Optional.ofNullable(uploadProfileImageResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(UploadProfileImageResponse.builder().build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "프로필 사진 응답")
  public static class UploadProfileImageResponse extends BaseResponse<GlobalErrorCode> {
  }
}
