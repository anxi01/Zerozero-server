package com.zerozero.auth.application;

import com.zerozero.auth.application.LoginUserUseCase.LoginUserRequest;
import com.zerozero.auth.application.LoginUserUseCase.LoginUserResponse;
import com.zerozero.auth.application.LoginUserUseCase.LoginUserResponse.Tokens;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class LoginUserUseCase implements BaseUseCase<LoginUserRequest, LoginUserResponse> {

  private final AuthenticationManager authenticationManager;

  private final JwtUtil jwtUtil;

  private final UserJPARepository userJPARepository;

  private final RefreshTokenJPARepository refreshTokenJPARepository;

  @Override
  public LoginUserResponse execute(LoginUserRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[LoginUserUseCase] Invalid register request");
      return LoginUserResponse.builder().success(false)
          .errorCode(LoginUserErrorCode.NOT_EXIST_LOGIN_CONDITION).build();
    }
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    } catch (AuthenticationException e) {
      return LoginUserResponse.builder().success(false)
          .errorCode(LoginUserErrorCode.AUTHENTICATION_FAILED).build();
    }
    User user = userJPARepository.findByEmail(request.getEmail());
    if (user == null) {
      return LoginUserResponse.builder().success(false)
          .errorCode(LoginUserErrorCode.NOT_EXIST_USER).build();
    }
    AccessToken accessToken = jwtUtil.generateAccessToken(null, user);
    RefreshToken refreshToken = jwtUtil.generateRefreshToken(null, user);
    com.zerozero.core.domain.entity.RefreshToken optionalRefreshToken = refreshTokenJPARepository.findByUserId(user.getId());
    if (optionalRefreshToken == null) {
      com.zerozero.core.domain.entity.RefreshToken refreshTokenEntity = refreshToken.toEntity();
      refreshTokenEntity.setUserId(user.getId());
      refreshTokenJPARepository.save(refreshTokenEntity);
    } else {
      optionalRefreshToken.update(refreshToken.getToken());
    }
    return LoginUserResponse.builder()
        .tokens(Tokens.builder().accessToken(accessToken).refreshToken(refreshToken).build())
        .build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum LoginUserErrorCode implements BaseErrorCode<DomainException> {

    NOT_EXIST_LOGIN_CONDITION(HttpStatus.BAD_REQUEST, "로그인 요청 조건이 존재하지 않습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");

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
  public static class LoginUserResponse extends BaseResponse<LoginUserErrorCode> {

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
  public static class LoginUserRequest implements BaseRequest {

    private String email;

    private String password;
  }

}
