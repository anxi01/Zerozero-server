package com.zerozero.auth.presentation;

import com.zerozero.auth.application.LoginUserUseCase;
import com.zerozero.auth.application.LoginUserUseCase.LoginUserResponse.Tokens;
import com.zerozero.auth.presentation.LoginUserController.LoginUserResponse.Token;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.RefreshToken;
import com.zerozero.core.exception.error.GlobalErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자")
public class LoginUserController {

  private final LoginUserUseCase loginUserUseCase;

  @Operation(
      summary = "사용자 로그인 API",
      description = "사용자가 이메일과 비밀번호를 입력하여 로그인합니다.",
      operationId = "/login"
  )
  @PostMapping("/login")
  public ResponseEntity<LoginUserResponse> loginUser(@Valid @RequestBody LoginUserRequest loginUserRequest) {
    LoginUserUseCase.LoginUserResponse loginUserResponse = loginUserUseCase.execute(
        LoginUserUseCase.LoginUserRequest.builder()
            .email(loginUserRequest.getEmail())
            .password(loginUserRequest.getPassword())
            .build());
    if (loginUserResponse == null || !loginUserResponse.isSuccess()) {
      Optional.ofNullable(loginUserResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(LoginUserResponse.builder()
        .token(new Token(
            Optional.ofNullable(loginUserResponse.getTokens()).map(Tokens::getAccessToken)
                .orElse(null),
            Optional.ofNullable(loginUserResponse.getTokens()).map(Tokens::getRefreshToken)
                .orElse(null)))
        .build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "사용자 로그인 응답")
  public static class LoginUserResponse extends BaseResponse<GlobalErrorCode> {

    @Schema(description = "액세스 토큰과 리프레시 토큰")
    private Token token;

    record Token(AccessToken accessToken, RefreshToken refreshToken) {
    }
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "사용자 로그인 요청")
  public static class LoginUserRequest implements BaseRequest {

    @NotNull(message = "이메일은 필수 데이터입니다.")
    @Email
    @Schema(description = "이메일", example = "zerozero@drink.com")
    private String email;

    @NotNull(message = "비밀번호는 필수 데이터입니다.")
    @Schema(description = "비밀번호", example = "zero")
    private String password;
  }
}
