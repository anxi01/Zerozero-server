package com.zerozero.auth.application;

import com.zerozero.auth.application.HandleOAuthLoginUseCase.HandleOAuthLoginRequest;
import com.zerozero.auth.application.HandleOAuthLoginUseCase.HandleOAuthLoginResponse;
import com.zerozero.auth.application.HandleOAuthLoginUseCase.HandleOAuthLoginResponse.Token;
import com.zerozero.auth.exception.AuthenticationErrorCode;
import com.zerozero.auth.infrastructure.oauth.OAuthRestClient;
import com.zerozero.auth.infrastructure.oauth.OAuthRestClientFactory;
import com.zerozero.auth.infrastructure.oauth.common.OAuthAccessTokenResponse;
import com.zerozero.auth.infrastructure.oauth.common.OAuthResourceResponse;
import com.zerozero.auth.infrastructure.oauth.common.Provider;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.Status;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.RefreshTokenJPARepository;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.RefreshToken;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.core.util.JwtUtil;
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

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class HandleOAuthLoginUseCase implements BaseUseCase<HandleOAuthLoginRequest, HandleOAuthLoginResponse> {

  private final OAuthRestClientFactory oAuthRestClientFactory;

  private final UserJPARepository userJPARepository;

  private final JwtUtil jwtUtil;

  private final RefreshTokenJPARepository refreshTokenJPARepository;

  @Override
  public HandleOAuthLoginResponse execute(HandleOAuthLoginRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[HandleOAuthLoginUseCase] Invalid request");
      return HandleOAuthLoginResponse.builder()
          .success(false)
          .errorCode(HandleOAuthLoginErrorCode.NOT_EXIST_LOGIN_CONDITION)
          .build();
    }
    Provider provider = Provider.valueOf(request.providerName.toUpperCase());
    OAuthRestClient oAuthRestClient = oAuthRestClientFactory.getOAuthRestClient(provider);

    OAuthAccessTokenResponse oAuthAccessTokenResponse = oAuthRestClient.getAccessToken(request.code);
    if (oAuthAccessTokenResponse == null) {
      log.error("[HandleOAuthLoginUseCase] OAuth access token is null");
      throw AuthenticationErrorCode.ACCESS_TOKEN_NOT_ISSUED.toException();
    }
    OAuthResourceResponse oAuthResourceResponse = oAuthRestClient.getResource(oAuthAccessTokenResponse.accessToken());
    if (oAuthResourceResponse == null) {
      log.error("[HandleOAuthLoginUseCase] OAuth resource is null");
      throw AuthenticationErrorCode.NOT_EXIST_RESOURCE_RESPONSE.toException();
    }

    String userEmail = oAuthResourceResponse.email();
    User user = userJPARepository.findByEmail(userEmail);
    if (user == null) {
      User pendingUser = User.builder()
          .email(userEmail)
          .status(Status.PENDING)
          .build();
      userJPARepository.save(pendingUser);
      return generateAndBuildResponse(pendingUser);
    } else {
      return generateAndBuildResponse(user);
    }
  }

  private HandleOAuthLoginResponse generateAndBuildResponse(User user) {
    AccessToken accessToken = jwtUtil.generateAccessToken(user);
    RefreshToken refreshToken = jwtUtil.generateRefreshToken(user);
    refreshTokenJPARepository.save(refreshToken.toEntity());

    return HandleOAuthLoginResponse.builder()
        .user(com.zerozero.core.domain.vo.User.of(user))
        .token(Token.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build())
        .build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum HandleOAuthLoginErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_LOGIN_CONDITION(HttpStatus.BAD_REQUEST, "로그인 요청 조건이 존재하지 않습니다."),
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
  public static class HandleOAuthLoginResponse extends BaseResponse<HandleOAuthLoginErrorCode> {

    private com.zerozero.core.domain.vo.User user;

    private Token token;

    @ToString
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Token {

      private AccessToken accessToken;

      private RefreshToken refreshToken;
    }
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class HandleOAuthLoginRequest implements BaseRequest {

    private String providerName;

    private String code;

    @Override
    public boolean isValid() {
      return providerName != null && !providerName.isEmpty() && code != null && !code.isEmpty();
    }
  }
}
