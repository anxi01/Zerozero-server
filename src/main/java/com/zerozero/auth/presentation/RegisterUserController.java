package com.zerozero.auth.presentation;

import com.zerozero.auth.application.RegisterUserUseCase;
import com.zerozero.auth.exception.AuthErrorType;
import com.zerozero.auth.presentation.request.RegisterRequest;
import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.support.error.GlobalErrorType;
import com.zerozero.core.support.response.ApiResponse;
import com.zerozero.user.domain.response.UserResponse;
import com.zerozero.user.exception.UserErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    @ApiErrorCode({GlobalErrorType.class, AuthErrorType.class, UserErrorType.class})
    @Authorization
    @PostMapping("/register")
    public ApiResponse<UserResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest,
                                                  @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
        UserResponse userResponse = registerUserUseCase.execute(registerRequest, authorizationHeader.substring(7));
        return ApiResponse.success(userResponse);
    }

}
