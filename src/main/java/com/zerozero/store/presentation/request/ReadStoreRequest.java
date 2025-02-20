package com.zerozero.store.presentation.request;

import com.zerozero.review.domain.model.ReviewFilter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ReadStoreRequest(
        @Schema(description = "판매점 ID", example = "11ef3e05-f45b-7e6c-a084-7b554bfaa162")
        UUID storeId,

        @Schema(description = "리뷰 정렬 조건 : RECENT(최신순), RECOMMEND(추천순)", example = "RECENT")
        ReviewFilter filter
) {
}
