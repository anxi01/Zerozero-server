package com.zerozero.store.presentation.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank

@JvmRecord
data class StoreSearchRequest(

    @Schema(description = "판매점 검색 쿼리", example = "꿉당")
    @field:NotBlank(message = "판매점 검색어는 필수 값입니다.")
    val query: String,

    @Schema(description = "사용자 경도", example = "127.01727639915623")
    @field:DecimalMin(value = "-180.0", message = "경도는 -180.0 이상이어야 합니다.")
    @field:DecimalMax(value = "180.0", message = "경도는 180.0 이하여야 합니다.")
    val longitude: Double,

    @Schema(description = "사용자 위도", example = "37.4839596934158")
    @field:DecimalMin(value = "-90.0", message = "위도는 -90.0 이상이어야 합니다.")
    @field:DecimalMax(value = "90.0", message = "위도는 90.0 이하여야 합니다.")
    val latitude: Double
)
