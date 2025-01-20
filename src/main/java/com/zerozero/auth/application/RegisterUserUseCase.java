package com.zerozero.auth.application;

import com.zerozero.auth.application.RegisterUserUseCase.RegisterUserRequest;
import com.zerozero.auth.application.RegisterUserUseCase.RegisterUserResponse;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.User;
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
@Transactional
public class RegisterUserUseCase implements BaseUseCase<RegisterUserRequest, RegisterUserResponse> {

  @Override
  public RegisterUserResponse execute(RegisterUserRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[RegisterUserUseCase] Invalid register request");
      return RegisterUserResponse.builder().success(false)
          .errorCode(RegisterUserErrorCode.NOT_EXIST_REGISTER_CONDITION).build();
    }
    User user = request.user;
    user.completePendingUser(request.getNickname());
    return RegisterUserResponse.builder()
        .user(com.zerozero.core.domain.vo.User.of(user))
        .build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum RegisterUserErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REGISTER_CONDITION(HttpStatus.BAD_REQUEST, "회원가입 조건이 올바르지 않습니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
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
  public static class RegisterUserResponse extends BaseResponse<RegisterUserErrorCode> {
    private com.zerozero.core.domain.vo.User user;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class RegisterUserRequest implements BaseRequest {
    private User user;

    private String nickname;

    @Override
    public boolean isValid() {
      return user != null && nickname != null && !nickname.isEmpty();
    }
  }

}
