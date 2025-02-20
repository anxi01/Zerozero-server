package com.zerozero.review.presentation;

import com.zerozero.configuration.argumentresolver.LoginUser;
import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.support.error.GlobalErrorType;
import com.zerozero.core.support.response.ApiResponse;
import com.zerozero.review.domain.service.LikeStoreReviewUseCase;
import com.zerozero.review.exception.ReviewErrorType;
import com.zerozero.user.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰")
public class LikeStoreReviewController {

    private final LikeStoreReviewUseCase likeStoreReviewUseCase;

    @Operation(
            summary = "리뷰 좋아요 API",
            description = "사용자가 리뷰 ID를 통해 좋아요를 누릅니다.",
            operationId = "/review/like/{reviewId}"
    )
    @ApiErrorCode({GlobalErrorType.class, ReviewErrorType.class})
    @Authorization
    @PatchMapping("/review/like/{reviewId}")
    public ApiResponse<?> likeStoreReview(@PathVariable(name = "reviewId") @Schema(description = "리뷰 ID") UUID reviewId,
                                          @Parameter(hidden = true) @LoginUser User user) {
        likeStoreReviewUseCase.execute(reviewId, user);
        return ApiResponse.success();
    }

}
