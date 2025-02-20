package com.zerozero.user.presentation;

import com.zerozero.configuration.argumentresolver.LoginUser;
import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.support.error.GlobalErrorType;
import com.zerozero.core.support.response.ApiResponse;
import com.zerozero.user.application.UserService;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.exception.UserErrorType;
import com.zerozero.user.presentation.response.ReadUserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자")
public class ReadUserInfoController {

    private final UserService userService;

    @Operation(
            summary = "마이페이지 조회 API",
            description = "사용자의 마이페이지를 토큰을 통해 조회합니다.",
            operationId = "/user/mypage"
    )
    @ApiErrorCode({GlobalErrorType.class, UserErrorType.class})
    @Authorization
    @GetMapping("/user/mypage")
    public ApiResponse<ReadUserInfoResponse> readUserInfo(@Parameter(hidden = true) @LoginUser User user) {
        return ApiResponse.success(userService.readUserInfo(user));
    }
}
