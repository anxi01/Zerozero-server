package com.zerozero.review.presentation

import com.zerozero.configuration.argumentresolver.LoginUser
import com.zerozero.configuration.interceptor.Authorization
import com.zerozero.configuration.swagger.ApiErrorCode
import com.zerozero.core.support.error.GlobalErrorType
import com.zerozero.core.support.response.ApiResponse
import com.zerozero.review.domain.service.UpdateStoreReviewUseCase
import com.zerozero.review.exception.ReviewErrorType
import com.zerozero.review.presentation.request.ReviewRequest
import com.zerozero.user.domain.model.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@Tag(name = "Review", description = "리뷰")
@RestController
class UpdateStoreReviewController(
    private val updateStoreReviewUseCase: UpdateStoreReviewUseCase
) {

    @Operation(
        summary = "리뷰 수정 API",
        description = "리뷰 ID를 통해 리뷰를 수정합니다.",
        operationId = "/review/{reviewId}"
    )
    @ApiErrorCode(
        GlobalErrorType::class, ReviewErrorType::class
    )
    @Authorization
    @PatchMapping("/review/{reviewId}")
    fun updateStoreReview(
        @PathVariable(name = "reviewId") @Schema(description = "리뷰 ID") reviewId: UUID,
        @Valid @RequestBody reviewRequest: ReviewRequest,
        @Parameter(hidden = true) @LoginUser user: User
    ): ApiResponse<Any> {
        updateStoreReviewUseCase.execute(reviewId, reviewRequest, user)
        return ApiResponse.success()
    }
}
