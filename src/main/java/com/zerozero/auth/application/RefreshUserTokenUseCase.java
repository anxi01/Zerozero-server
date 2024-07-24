package com.zerozero.auth.application;

import com.zerozero.auth.application.RefreshUserTokenUseCase.RefreshUserTokenRequest;
import com.zerozero.auth.application.RefreshUserTokenUseCase.RefreshUserTokenResponse;
import com.zerozero.auth.application.RefreshUserTokenUseCase.RefreshUserTokenResponse.Tokens;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
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

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshUserTokenUseCase implements BaseUseCase<RefreshUserTokenRequest, RefreshUserTokenResponse> {

  private final JwtUtil jwtUtil;

  private final UserJPARepository userJPARepository;

  private final RefreshTokenJPARepository refreshTokenJPARepository;

  @Override
  public RefreshUserTokenResponse execute(RefreshUserTokenRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[RefreshUserTokenUseCase] RefreshToken is invalid");
      return RefreshUserTokenResponse.builder().success(false)
          .errorCode(RefreshUserTokenErrorCode.NOT_EXIST_REFRESH_TOKEN).build();
    }
    RefreshToken refreshToken = request.getRefreshToken();
    if (jwtUtil.isTokenExpired(refreshToken.getToken())) {
      log.error("[RefreshUserTokenUseCase] Expired access token");
      return RefreshUserTokenResponse.builder().success(false)
          .errorCode(RefreshUserTokenErrorCode.EXPIRED_TOKEN)
          .build();
    }
    String userEmail = jwtUtil.extractUsername(refreshToken.getToken());
    if (userEmail == null) {
      log.error("[RefreshUserTokenUseCase] User email not exist");
      return RefreshUserTokenResponse.builder().success(false)
          .errorCode(RefreshUserTokenErrorCode.NOT_EXIST_USER_EMAIL).build();
    }
    User user = userJPARepository.findByEmail(userEmail);
    if (user == null) {
      log.error("[RefreshUserTokenUseCase] User not exist");
      return RefreshUserTokenResponse.builder().success(false)
          .errorCode(RefreshUserTokenErrorCode.NOT_EXIST_USER).build();
    }
    com.zerozero.core.domain.entity.RefreshToken alreadyExistRefreshToken = refreshTokenJPARepository.findByUserId(user.getId());
    if (alreadyExistRefreshToken == null) {
      log.error("[RefreshUserTokenUseCase] Refresh token not exist");
      return RefreshUserTokenResponse.builder().success(false)
          .errorCode(RefreshUserTokenErrorCode.NOT_EXIST_REFRESH_TOKEN).build();
    }
    if (RefreshToken.of(alreadyExistRefreshToken).equals(refreshToken)
        && jwtUtil.isTokenValid(refreshToken.getToken(), user)) {
      AccessToken accessToken = jwtUtil.generateAccessToken(null, user);
      return RefreshUserTokenResponse.builder()
          .tokens(Tokens.builder().accessToken(accessToken).refreshToken(refreshToken).build())
          .build();
    }
    return RefreshUserTokenResponse.builder().success(false)
        .errorCode(RefreshUserTokenErrorCode.TOKEN_REFRESH_FAILED).build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum RefreshUserTokenErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰이 존재하지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    NOT_EXIST_USER_EMAIL(HttpStatus.BAD_REQUEST, "토큰에 사용자 메일이 존재하지 않습니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    TOKEN_REFRESH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 재발급에 실패하였습니다.");

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
  public static class RefreshUserTokenResponse extends BaseResponse<RefreshUserTokenErrorCode> {

    private Tokens tokens;

    @ToString
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Tokens {

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
  public static class RefreshUserTokenRequest implements BaseRequest {

    private RefreshToken refreshToken;

    @Override
    public boolean isValid() {
      return refreshToken != null;
    }
  }

}
