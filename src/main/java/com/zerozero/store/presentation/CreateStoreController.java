package com.zerozero.store.presentation;

import com.zerozero.configuration.argumentresolver.LoginUser;
import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.support.error.GlobalErrorType;
import com.zerozero.core.support.response.ApiResponse;
import com.zerozero.store.domain.service.CreateStoreUseCase;
import com.zerozero.store.exception.StoreErrorType;
import com.zerozero.store.presentation.request.CreateStoreRequest;
import com.zerozero.user.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Store", description = "판매점")
public class CreateStoreController {

    private final CreateStoreUseCase createStoreUseCase;

    @Operation(
            summary = "판매점 등록 API",
            description = "사용자가 검색한 판매점 ID를 통해 제로음료 판매점을 등록합니다.",
            operationId = "/store")
    @ApiErrorCode({GlobalErrorType.class, StoreErrorType.class})
    @Authorization
    @PostMapping("/store")
    public ApiResponse<UUID> createStore(@Valid @RequestBody CreateStoreRequest createStoreRequest, @Parameter(hidden = true) @LoginUser User user) {
        return ApiResponse.success(createStoreUseCase.execute(createStoreRequest, user));
    }

}
