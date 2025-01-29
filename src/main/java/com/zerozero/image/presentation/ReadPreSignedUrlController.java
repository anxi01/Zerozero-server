package com.zerozero.image.presentation;

import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.image.application.ReadPreSignedUrlUseCase;
import com.zerozero.image.application.ReadPreSignedUrlUseCase.ReadPreSignedUrlErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Image", description = "이미지")
public class ReadPreSignedUrlController {

  private final ReadPreSignedUrlUseCase readPreSignedUrlUseCase;

  @Operation(
      summary = "이미지 업로드 PreSigned URL 조회 API",
      description = "클라이언트에서 AWS S3 버킷에 이미지를 저장하기 위한 PreSigned URL을 조회합니다.",
      operationId = "/image/presigned-url"
  )
  @ApiErrorCode({GlobalErrorCode.class, ReadPreSignedUrlErrorCode.class})
  @Authorization
  @GetMapping("/image/presigned-url")
  public ResponseEntity<ReadPreSignedUrlResponse> readPreSignedUrl(@ParameterObject ReadPreSignedUrlRequest request) {
    ReadPreSignedUrlUseCase.ReadPreSignedUrlResponse readPreSignedUrlResponse = readPreSignedUrlUseCase.execute(
        ReadPreSignedUrlUseCase.ReadPreSignedUrlRequest.builder()
            .prefix(request.getPrefix())
            .fileName(request.getFileName())
            .build());
    if (readPreSignedUrlResponse == null || !readPreSignedUrlResponse.isSuccess()) {
      Optional.ofNullable(readPreSignedUrlResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(
        ReadPreSignedUrlResponse.builder()
            .preSignedUrl(readPreSignedUrlResponse.getPreSignedUrl())
            .objectUrl(readPreSignedUrlResponse.getObjectUrl())
            .build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "이미지 업로드 PreSigned URL 조회 응답")
  public static class ReadPreSignedUrlResponse extends BaseResponse<GlobalErrorCode> {

    @Schema(description = "이미지 업로드 PreSigned URL", example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/0cbf3b99-b0ba-4148-a891-d04cb71ae236-test.png")
    private String preSignedUrl;

    @Schema(description = "이미지 객체 URL", example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/0cbf3b99-b0ba-4148-a891-d04cb71ae236-test.png")
    private String objectUrl;
  }


  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "이미지 업로드 PreSigned URL 조회 요청")
  public static class ReadPreSignedUrlRequest implements BaseRequest {

    @Schema(description = "이미지 접두사 ['store', 'user']", example = "store")
    private String prefix;

    @Schema(description = "업로드된 이미지 파일명", example = "profile-image.png")
    private String fileName;
  }
}
