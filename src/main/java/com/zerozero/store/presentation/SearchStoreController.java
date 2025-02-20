package com.zerozero.store.presentation;

import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.support.error.GlobalErrorType;
import com.zerozero.core.support.response.ApiResponse;
import com.zerozero.store.domain.response.StoreResponse;
import com.zerozero.store.domain.service.SearchStoreUseCase;
import com.zerozero.store.exception.StoreErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Store", description = "판매점")
public class SearchStoreController {

    private final SearchStoreUseCase searchStoreUseCase;

    @Operation(
            summary = "[가게제보] 판매점 검색 API",
            description = "가게제보 페이지에서 등록할 판매점을 쿼리를 통해 검색합니다.",
            operationId = "/store/search"
    )
    @ApiErrorCode({GlobalErrorType.class, StoreErrorType.class})
    @Authorization
    @GetMapping("/store/search")
    public ApiResponse<List<StoreResponse>> searchStore(@Parameter(description = "판매점 검색 쿼리", example = "꿉당") @RequestParam String query) {
        List<StoreResponse> storeResponses = searchStoreUseCase.execute(query);
        return ApiResponse.success(storeResponses);
    }

}
