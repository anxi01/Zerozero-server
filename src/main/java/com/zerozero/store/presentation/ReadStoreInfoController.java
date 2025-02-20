package com.zerozero.store.presentation;

import com.zerozero.configuration.argumentresolver.LoginUser;
import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.support.error.GlobalErrorType;
import com.zerozero.core.support.response.ApiResponse;
import com.zerozero.store.application.StoreService;
import com.zerozero.store.exception.StoreErrorType;
import com.zerozero.store.presentation.request.ReadStoreRequest;
import com.zerozero.store.presentation.response.ReadStoreResponse;
import com.zerozero.user.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Store", description = "판매점")
public class ReadStoreInfoController {

    private final StoreService storeService;

    @Operation(
            summary = "판매점 조회 API",
            description = "판매점 ID를 통해 판매점과 리뷰를 조회합니다.",
            operationId = "/store"
    )
    @ApiErrorCode({GlobalErrorType.class, StoreErrorType.class})
    @Authorization
    @GetMapping("/store")
    public ApiResponse<ReadStoreResponse> readStoreInfo(@ParameterObject ReadStoreRequest readStoreRequest, @Parameter(hidden = true) @LoginUser User user) {
        ReadStoreResponse readStoreResponse = storeService.readStore(readStoreRequest, user);
        return ApiResponse.success(readStoreResponse);
    }

}
