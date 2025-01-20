package com.zerozero.auth.presentation;

import com.zerozero.auth.application.HandleOAuthLoginUseCase;
import com.zerozero.auth.application.HandleOAuthLoginUseCase.HandleOAuthLoginErrorCode;
import com.zerozero.auth.presentation.HandleOAuthLoginController.HandleOAuthLoginResponse.Token;
import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.User;
import com.zerozero.core.exception.error.GlobalErrorCode;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자")
public class HandleOAuthLoginController {

  private final HandleOAuthLoginUseCase handleOAuthLoginUseCase;

  @Operation(
      summary = "사용자 소셜 로그인 API",
      description = "인가 코드를 사용하여 사용자의 로그인을 처리합니다. \n\n 회원가입이 완료된 상태(COMPLETED)라면 메인 페이지로 리다이렉트되며, 가입 과정에서 중단된 사용자(PENDING)는 회원가입 페이지로 리다이렉트됩니다.",
      operationId = "/login"
  )
  @ApiErrorCode({GlobalErrorCode.class, HandleOAuthLoginErrorCode.class})
  @GetMapping("/login/{providerName}")
  public ResponseEntity<HandleOAuthLoginResponse> handleOAuthLogin(
      @ParameterObject HandleOAuthLoginRequest request, @PathVariable String providerName) {
    HandleOAuthLoginUseCase.HandleOAuthLoginResponse handleOAuthLoginResponse = handleOAuthLoginUseCase.execute(
        HandleOAuthLoginUseCase.HandleOAuthLoginRequest.builder()
            .code(request.getCode())
            .providerName(providerName)
            .build());
    if (handleOAuthLoginResponse == null || !handleOAuthLoginResponse.isSuccess()) {
      Optional.ofNullable(handleOAuthLoginResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(
        HandleOAuthLoginResponse.builder()
            .user(handleOAuthLoginResponse.getUser())
            .token(new Token(handleOAuthLoginResponse.getToken().getAccessToken().getToken(),
                handleOAuthLoginResponse.getToken().getRefreshToken().getToken()))
            .build());
  }


  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "사용자 소셜 로그인 응답")
  public static class HandleOAuthLoginResponse extends BaseResponse<GlobalErrorCode> {

    private User user;

    @Schema(description = "액세스 토큰과 리프레시 토큰")
    private Token token;

    record Token(String accessToken, String refreshToken) {

    }
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "사용자 소셜 로그인 요청")
  public static class HandleOAuthLoginRequest implements BaseRequest {

    @Schema(description = "소셜 로그인 시 사용되는 인가 코드")
    private String code;
  }
}
