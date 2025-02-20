package com.zerozero.store.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record StoreSearchRequest(

        @Schema(description = "판매점 검색 쿼리", example = "꿉당")
        String query,

        @Schema(description = "사용자 경도", example = "127.01727639915623")
        double longitude,

        @Schema(description = "사용자 위도", example = "37.4839596934158")
        double latitude
) {
}
