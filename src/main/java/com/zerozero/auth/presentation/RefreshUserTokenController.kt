package com.zerozero.auth.presentation

import com.zerozero.auth.application.RefreshUserTokenService
import com.zerozero.auth.exception.AuthErrorType
import com.zerozero.auth.presentation.response.TokenResponse
import com.zerozero.configuration.swagger.ApiErrorCode
import com.zerozero.core.support.error.GlobalErrorType
import com.zerozero.core.support.response.ApiResponse
import com.zerozero.user.exception.UserErrorType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth", description = "인증/인가")
@RestController
class RefreshUserTokenController(
    private val refreshUserTokenService: RefreshUserTokenService
) {

    @Operation(
        summary = "토큰 재발급 API",
        description = "사용자의 리프레시 토큰을 통해 토큰을 재발급합니다.",
        operationId = "/refresh/token"
    )
    @ApiErrorCode(
        GlobalErrorType::class, AuthErrorType::class, UserErrorType::class
    )
    @GetMapping("/refresh/token")
    fun refreshUserToken(
        @RequestParam refreshToken: String
    ): ApiResponse<TokenResponse> {
        val tokenResponse = refreshUserTokenService.execute(refreshToken)
        return ApiResponse.success(tokenResponse)
    }
}
