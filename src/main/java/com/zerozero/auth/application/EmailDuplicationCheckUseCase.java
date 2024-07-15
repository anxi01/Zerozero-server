package com.zerozero.auth.application;

import com.zerozero.auth.application.EmailDuplicationCheckUseCase.EmailDuplicationCheckRequest;
import com.zerozero.auth.application.EmailDuplicationCheckUseCase.EmailDuplicationCheckResponse;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailDuplicationCheckUseCase implements BaseUseCase<EmailDuplicationCheckRequest, EmailDuplicationCheckResponse> {

  private final UserJPARepository userJPARepository;

  @Override
  public EmailDuplicationCheckResponse execute(EmailDuplicationCheckRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[EmailDuplicationCheckUseCase] Email is invalid");
      return EmailDuplicationCheckResponse.builder().success(false)
          .errorCode(EmailDuplicationCheckErrorCode.NOT_EXIST_EMAIL_CONDITION)
          .build();
    }
    Boolean isUserEmailDuplicated = userJPARepository.existsByEmail(request.getEmail());
    if (isUserEmailDuplicated == null) {
      log.error("[EmailDuplicationCheckUseCase] Failed to check email duplication");
      return EmailDuplicationCheckResponse.builder().success(false)
          .errorCode(EmailDuplicationCheckErrorCode.FAILED_EMAIL_DUPLICATION_CHECK)
          .build();
    }
    if (isUserEmailDuplicated) {
      log.error("[EmailDuplicationCheckUseCase] Email already exists");
      return EmailDuplicationCheckResponse.builder().success(false)
          .errorCode(EmailDuplicationCheckErrorCode.EMAIL_ALREADY_EXISTS)
          .build();
    }
    return EmailDuplicationCheckResponse.builder().build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum EmailDuplicationCheckErrorCode implements BaseErrorCode<DomainException> {

    NOT_EXIST_EMAIL_CONDITION(HttpStatus.BAD_REQUEST, "이메일 양식이 올바르지 않습니다."),
    FAILED_EMAIL_DUPLICATION_CHECK(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 중복 검증에 실패하였습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이메일이 이미 존재합니다.");

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
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class EmailDuplicationCheckResponse extends BaseResponse<EmailDuplicationCheckErrorCode> {
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class EmailDuplicationCheckRequest implements BaseRequest {

    private String email;

    @Override
    public boolean isValid() {
      return email != null && !email.isEmpty();
    }
  }

}
