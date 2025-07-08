package com.zerozero.store.presentation

import com.zerozero.configuration.argumentresolver.LoginUser
import com.zerozero.configuration.interceptor.Authorization
import com.zerozero.configuration.swagger.ApiErrorCode
import com.zerozero.core.support.error.GlobalErrorType
import com.zerozero.core.support.response.ApiResponse
import com.zerozero.store.domain.response.StoreResponse
import com.zerozero.store.domain.service.ReadUserStoresUseCase
import com.zerozero.user.domain.model.User
import com.zerozero.user.exception.UserErrorType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User", description = "사용자")
@RestController
class ReadUserStoresController(
    private val readUserStoresUseCase: ReadUserStoresUseCase
) {

    @Operation(
        summary = "사용자가 등록한 판매점 목록 조회 API",
        description = "사용자가 등록한 판매점 목록을 조회합니다.",
        operationId = "/user/stores"
    )
    @ApiErrorCode(
        GlobalErrorType::class, UserErrorType::class
    )
    @Authorization
    @GetMapping("/user/stores")
    fun readUserStores(
        @Parameter(hidden = true) @LoginUser user: User
    ): ApiResponse<List<StoreResponse>> {
        val storeResponses = readUserStoresUseCase.execute(user)
        return ApiResponse.success(storeResponses)
    }
}
