package com.zerozero.auth.presentation;

import com.zerozero.auth.application.HandleOAuthLoginUseCase;
import com.zerozero.auth.exception.AuthErrorType;
import com.zerozero.auth.presentation.response.LoginResponse;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.support.error.GlobalErrorType;
import com.zerozero.core.support.response.ApiResponse;
import com.zerozero.user.exception.UserErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
    @ApiErrorCode({GlobalErrorType.class, AuthErrorType.class, UserErrorType.class})
    @GetMapping("/login/{providerName}")
    public ApiResponse<LoginResponse> handleOAuthLogin(
            @RequestParam @Schema(description = "소셜 로그인 시 사용되는 인가 코드") String code,
            @PathVariable String providerName) {
        LoginResponse loginResponse = handleOAuthLoginUseCase.execute(code, providerName);
        return ApiResponse.success(loginResponse);
    }

}
