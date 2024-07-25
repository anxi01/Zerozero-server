package com.zerozero.auth.presentation;

import com.zerozero.auth.application.EmailDuplicationCheckUseCase;
import com.zerozero.auth.application.EmailDuplicationCheckUseCase.EmailDuplicationCheckErrorCode;
import com.zerozero.auth.application.EmailDuplicationCheckUseCase.EmailDuplicationCheckRequest;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.exception.error.GlobalErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자")
public class EmailDuplicationCheckController {

  private final EmailDuplicationCheckUseCase emailDuplicationCheckUseCase;

  @Operation(
      summary = "이메일 중복 검증 API",
      description = "사용자 이메일이 중복인지 검증합니다.",
      operationId = "/email/duplicate-check"
  )
  @ApiErrorCode({GlobalErrorCode.class, EmailDuplicationCheckErrorCode.class})
  @GetMapping("/email/duplicate-check")
  public ResponseEntity<EmailDuplicationCheckResponse> emailDuplicationCheck(
      @Schema(description = "이메일", example = "zerozero@drink.com") @Valid @Email String email) {
    EmailDuplicationCheckUseCase.EmailDuplicationCheckResponse emailDuplicationCheckResponse = emailDuplicationCheckUseCase.execute(
        EmailDuplicationCheckRequest.builder()
            .email(email)
            .build());
    if (emailDuplicationCheckResponse == null || !emailDuplicationCheckResponse.isSuccess()) {
      Optional.ofNullable(emailDuplicationCheckResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok().build();
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @Schema(description = "이메일 중복 검증 응답")
  public static class EmailDuplicationCheckResponse extends BaseResponse<GlobalErrorCode> {
  }
}
