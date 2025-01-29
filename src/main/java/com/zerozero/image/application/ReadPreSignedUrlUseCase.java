package com.zerozero.image.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.core.util.AWSS3Service;
import com.zerozero.core.util.AWSS3Service.Prefix;
import com.zerozero.image.application.ReadPreSignedUrlUseCase.ReadPreSignedUrlRequest;
import com.zerozero.image.application.ReadPreSignedUrlUseCase.ReadPreSignedUrlResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReadPreSignedUrlUseCase implements BaseUseCase<ReadPreSignedUrlRequest, ReadPreSignedUrlResponse> {

  private final AWSS3Service awss3Service;

  @Override
  public ReadPreSignedUrlResponse execute(ReadPreSignedUrlRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[ReadPreSignedUrlResponse] Invalid request");
      return ReadPreSignedUrlResponse.builder()
          .success(false)
          .errorCode(ReadPreSignedUrlErrorCode.NOT_EXIST_REQUEST_CONDITION)
          .build();
    }
    Prefix prefix = Prefix.fromString(request.getPrefix());
    if (prefix == null) {
      log.error("[ReadPreSignedUrlResponse] Invalid prefix: {}", request.getPrefix());
      return ReadPreSignedUrlResponse.builder()
          .success(false)
          .errorCode(ReadPreSignedUrlErrorCode.INVALID_PREFIX)
          .build();
    }
    String preSignedUrl = awss3Service.getPreSignedUrl(prefix.getPrefix(), request.getFileName());
    String objectUrl = awss3Service.getObjectUrlFromPreSignedUrl(preSignedUrl);
    if (preSignedUrl == null || objectUrl == null) {
      log.error("[ReadPreSignedUrlResponse] Generated URLs(PreSigned, Object) are null");
      return ReadPreSignedUrlResponse.builder()
          .success(false)
          .errorCode(ReadPreSignedUrlErrorCode.FAILED_TO_MAKE_URL)
          .build();
    }
    log.info("[ReadPreSignedUrlResponse] Generated URLs - PreSigned: {}, Object: {}", preSignedUrl, objectUrl);
    return ReadPreSignedUrlResponse.builder()
        .preSignedUrl(preSignedUrl)
        .objectUrl(objectUrl)
        .build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum ReadPreSignedUrlErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REQUEST_CONDITION(HttpStatus.BAD_REQUEST, "요청 조건이 올바르지 않습니다."),
    INVALID_PREFIX(HttpStatus.BAD_REQUEST, "유효하지 않은 prefix 값입니다."),
    FAILED_TO_MAKE_URL(HttpStatus.INTERNAL_SERVER_ERROR, "PreSigned URL 생성에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
      return new DomainException(httpStatus, this);
    }
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadPreSignedUrlResponse extends BaseResponse<ReadPreSignedUrlErrorCode> {

    private String preSignedUrl;

    private String objectUrl;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadPreSignedUrlRequest implements BaseRequest {

    private String prefix;

    private String fileName;

    @Override
    public boolean isValid() {
      if (prefix == null || prefix.isEmpty() || fileName == null || fileName.isEmpty()) {
        return false;
      }
      String extension = FilenameUtils.getExtension(fileName).toUpperCase();
      return extension.equals("PNG") || extension.equals("JPG") || extension.equals("JPEG") || extension.equals("HEIC");
    }
  }
}
