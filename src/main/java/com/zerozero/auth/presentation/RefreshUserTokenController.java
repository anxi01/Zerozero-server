package com.zerozero.auth.presentation;

import com.zerozero.auth.application.RefreshUserTokenUseCase;
import com.zerozero.auth.application.RefreshUserTokenUseCase.RefreshUserTokenErrorCode;
import com.zerozero.auth.application.RefreshUserTokenUseCase.RefreshUserTokenResponse.Tokens;
import com.zerozero.auth.presentation.RefreshUserTokenController.RefreshUserTokenResponse.Token;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.RefreshToken;
import com.zerozero.core.exception.error.GlobalErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자")
public class RefreshUserTokenController {

  private final RefreshUserTokenUseCase refreshUserTokenUseCase;

  @Operation(
      summary = "토큰 재발급 API",
      description = "사용자의 리프레시 토큰을 통해 토큰을 재발급합니다.",
      operationId = "/refresh/token"
  )
  @ApiErrorCode({GlobalErrorCode.class, RefreshUserTokenErrorCode.class})
  @PostMapping("/refresh/token")
  public ResponseEntity<RefreshUserTokenResponse> refreshUserToken(@Parameter(hidden = true) RefreshToken refreshToken) {
    RefreshUserTokenUseCase.RefreshUserTokenResponse refreshUserTokenResponse = refreshUserTokenUseCase.execute(
        RefreshUserTokenUseCase.RefreshUserTokenRequest.builder()
            .refreshToken(refreshToken)
            .build());
    if (refreshUserTokenResponse == null || !refreshUserTokenResponse.isSuccess()) {
      Optional.ofNullable(refreshUserTokenResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(RefreshUserTokenResponse.builder()
        .token(new Token(
            Optional.ofNullable(refreshUserTokenResponse.getTokens()).map(Tokens::getAccessToken)
                .map(AccessToken::getToken)
                .orElse(null),
            Optional.ofNullable(refreshUserTokenResponse.getTokens()).map(Tokens::getRefreshToken)
                .map(RefreshToken::getToken)
                .orElse(null)))
        .build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "토큰 재발급 응답")
  public static class RefreshUserTokenResponse extends BaseResponse<GlobalErrorCode> {

    @Schema(description = "액세스 토큰과 리프레시 토큰")
    private Token token;

    record Token(String accessToken, String refreshToken) {
    }
  }
}
