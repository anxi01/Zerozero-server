package com.zerozero.store.presentation.response;

import com.zerozero.review.domain.model.ZeroDrink;
import com.zerozero.review.domain.response.ReviewResponse;
import com.zerozero.store.domain.response.StoreResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ReadStoreResponse(

        @Schema(description = "판매점 조회 정보")
        StoreResponse store,

        @Schema(description = "리뷰 목록")
        List<ReviewResponse> reviews,

        @Schema(description = "제로 음료수 순위")
        List<ZeroDrink> zeroDrinks
) {
    public static ReadStoreResponse of(StoreResponse storeResponse, List<ReviewResponse> reviews, List<ZeroDrink> zeroDrinks) {
        return new ReadStoreResponse(storeResponse, reviews, zeroDrinks);
    }
}
