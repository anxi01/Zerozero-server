package com.zerozero.store.presentation

import com.zerozero.configuration.argumentresolver.LoginUser
import com.zerozero.configuration.interceptor.Authorization
import com.zerozero.configuration.swagger.ApiErrorCode
import com.zerozero.core.support.error.GlobalErrorType
import com.zerozero.core.support.response.ApiResponse
import com.zerozero.store.domain.service.CreateStoreUseCase
import com.zerozero.store.exception.StoreErrorType
import com.zerozero.store.presentation.request.CreateStoreRequest
import com.zerozero.user.domain.model.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@Tag(name = "Store", description = "판매점")
@RestController
class CreateStoreController(
    private val createStoreUseCase: CreateStoreUseCase
) {

    @Operation(
        summary = "판매점 등록 API",
        description = "사용자가 검색한 판매점 ID를 통해 제로음료 판매점을 등록합니다.",
        operationId = "/store"
    )
    @ApiErrorCode(
        GlobalErrorType::class, StoreErrorType::class
    )
    @Authorization
    @PostMapping("/store")
    fun createStore(
        @Valid @RequestBody createStoreRequest: CreateStoreRequest,
        @Parameter(hidden = true) @LoginUser user: User
    ): ApiResponse<UUID> {
        val storeId = createStoreUseCase.execute(createStoreRequest, user)
        return ApiResponse.success(storeId)
    }
}
