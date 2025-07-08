package com.zerozero.store.presentation

import com.zerozero.configuration.interceptor.Authorization
import com.zerozero.configuration.swagger.ApiErrorCode
import com.zerozero.core.support.error.GlobalErrorType
import com.zerozero.core.support.response.ApiResponse
import com.zerozero.store.domain.response.StoreResponse
import com.zerozero.store.domain.service.SearchNearbyStoresUseCase
import com.zerozero.store.exception.StoreErrorType
import com.zerozero.store.presentation.request.StoreSearchRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Store", description = "판매점")
@RestController
class SearchNearbyStoresController(
    private val searchNearbyStoresUseCase: SearchNearbyStoresUseCase
) {

    @Operation(
        summary = "[메인페이지] 판매점 검색 API",
        description = "메인페이지에서 등록할 판매점을 쿼리와 경,위도를 통해 반경 2KM 내 판매점을 검색합니다.",
        operationId = "/store/search/nearby"
    )
    @ApiErrorCode(
        GlobalErrorType::class, StoreErrorType::class
    )
    @Authorization
    @GetMapping("/store/search/nearby")
    fun searchNearbyStores(
        @Valid @ParameterObject storeSearchRequest: StoreSearchRequest
    ): ApiResponse<List<StoreResponse>> {
        val storeResponses = searchNearbyStoresUseCase.execute(storeSearchRequest)
        return ApiResponse.success(storeResponses)
    }
}
