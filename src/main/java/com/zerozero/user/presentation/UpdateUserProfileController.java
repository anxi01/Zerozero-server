package com.zerozero.user.presentation;

import com.zerozero.configuration.argumentresolver.LoginUser;
import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.support.error.GlobalErrorType;
import com.zerozero.core.support.response.ApiResponse;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.domain.service.UpdateUserUseCase;
import com.zerozero.user.exception.UserErrorType;
import com.zerozero.user.presentation.request.UpdateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자")
public class UpdateUserProfileController {

    private final UpdateUserUseCase updateUserUseCase;

    @Operation(
            summary = "프로필 수정 API",
            description = "사용자가 이미지와 닉네임을 변경할 수 있습니다.",
            operationId = "/user"
    )
    @ApiErrorCode({GlobalErrorType.class, UserErrorType.class})
    @Authorization
    @PatchMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> uploadProfileImage(@Valid @RequestBody UpdateUserRequest updateUserRequest, @Parameter(hidden = true) @LoginUser User user) {
        updateUserUseCase.execute(updateUserRequest, user);
        return ApiResponse.success();
    }
}
