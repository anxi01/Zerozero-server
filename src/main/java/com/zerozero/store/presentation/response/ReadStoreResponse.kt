package com.zerozero.store.presentation.response

import com.zerozero.review.domain.model.ZeroDrink
import com.zerozero.review.domain.response.ReviewResponse
import com.zerozero.store.domain.response.StoreResponse
import io.swagger.v3.oas.annotations.media.Schema

@JvmRecord
data class ReadStoreResponse(

    @Schema(description = "판매점 조회 정보")
    val store: StoreResponse,

    @Schema(description = "리뷰 목록")
    val reviews: List<ReviewResponse>,

    @Schema(description = "제로 음료수 순위")
    val zeroDrinks: List<ZeroDrink>
) {
    companion object {
        @JvmStatic
        fun of(
            storeResponse: StoreResponse,
            reviews: List<ReviewResponse>,
            zeroDrinks: List<ZeroDrink>
        ) = ReadStoreResponse(storeResponse, reviews, zeroDrinks)
    }
}
