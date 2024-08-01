package com.zerozero.auth.application;

import com.zerozero.auth.application.RegisterUserUseCase.RegisterUserRequest;
import com.zerozero.auth.application.RegisterUserUseCase.RegisterUserResponse;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.entity.User.Role;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class RegisterUserUseCase implements BaseUseCase<RegisterUserRequest, RegisterUserResponse> {

  private final PasswordEncoder passwordEncoder;

  private final UserJPARepository userJPARepository;

  @Override
  public RegisterUserResponse execute(RegisterUserRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[RegisterUserUseCase] Invalid register request");
      return RegisterUserResponse.builder().success(false)
          .errorCode(RegisterUserErrorCode.NOT_EXIST_REGISTER_CONDITION).build();
    }
    if (isDuplicateEmail(request.getEmail())) {
      log.error("[RegisterUserUseCase] Email already exists");
      return RegisterUserResponse.builder().success(false)
          .errorCode(RegisterUserErrorCode.ALREADY_EXIST_EMAIL).build();
    }
    User user = User.builder()
        .nickname(request.getNickname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    userJPARepository.save(user);
    return RegisterUserResponse.builder().build();
  }

  private boolean isDuplicateEmail(String email) {
    return userJPARepository.existsByEmail(email);
  }

  @Getter
  @RequiredArgsConstructor
  public enum RegisterUserErrorCode implements BaseErrorCode<DomainException> {

    NOT_EXIST_REGISTER_CONDITION(HttpStatus.BAD_REQUEST, "회원가입 조건이 올바르지 않습니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");

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
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class RegisterUserRequest implements BaseRequest {

    private String nickname;

    private String email;

    private String password;

    @Override
    public boolean isValid() {
      return nickname != null && email != null && password != null;
    }
  }

}
