package com.zerozero.auth.presentation;

import com.zerozero.auth.application.RegisterUserUseCase;
import com.zerozero.auth.application.RegisterUserUseCase.RegisterUserErrorCode;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
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
public class RegisterUserController {

  private final RegisterUserUseCase registerUserUseCase;

  @Operation(
      summary = "사용자 회원가입 API",
      description = "사용자의 개인 정보를 통해 회원을 가입합니다.",
      operationId = "/register"
  )
  @ApiErrorCode({GlobalErrorCode.class, RegisterUserErrorCode.class})
  @PostMapping("/register")
  public ResponseEntity<RegisterUserResponse> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
    RegisterUserUseCase.RegisterUserResponse registerUserResponse = registerUserUseCase.execute(
        RegisterUserUseCase.RegisterUserRequest.builder()
            .nickname(registerUserRequest.getNickname())
            .email(registerUserRequest.getEmail())
            .password(registerUserRequest.getPassword())
            .build());
    if (registerUserResponse == null || !registerUserResponse.isSuccess()) {
      Optional.ofNullable(registerUserResponse)
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
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "사용자 회원가입 응답")
  public static class RegisterUserResponse extends BaseResponse<GlobalErrorCode> {
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "사용자 회원가입 요청")
  public static class RegisterUserRequest implements BaseRequest {

    @NotNull(message = "닉네임은 필수 데이터입니다.")
    @Schema(description = "닉네임", example = "제로")
    private String nickname;

    @NotNull(message = "이메일은 필수 데이터입니다.")
    @Email
    @Schema(description = "이메일", example = "zerozero@drink.com")
    private String email;

    @NotNull(message = "비밀번호는 필수 데이터입니다.")
    @Schema(description = "비밀번호", example = "zero")
    private String password;
  }
}
