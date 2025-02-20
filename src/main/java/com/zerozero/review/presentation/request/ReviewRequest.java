package com.zerozero.review.presentation.request;

import com.zerozero.review.domain.model.ZeroDrink;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record ReviewRequest(
        @NotBlank(message = "리뷰 내용은 비워둘 수 없습니다.")
        @Schema(description = "리뷰 내용", example = "제로콜라 판매 중!")
        String content,

        @NotEmpty(message = "최소 하나 이상의 제로음료를 선택해야 합니다.")
        @Schema(description = "제로 음료수 목록", example = "[\"COCA_COLA_ZERO\", \"PEPSI_ZERO\", \"SPRITE_ZERO\"]")
        Set<ZeroDrink> zeroDrinks
) {
}
