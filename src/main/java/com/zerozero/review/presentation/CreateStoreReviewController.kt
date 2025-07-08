package com.zerozero.review.presentation

import com.zerozero.configuration.argumentresolver.LoginUser
import com.zerozero.configuration.interceptor.Authorization
import com.zerozero.configuration.swagger.ApiErrorCode
import com.zerozero.core.support.error.GlobalErrorType
import com.zerozero.core.support.response.ApiResponse
import com.zerozero.review.application.ReviewService
import com.zerozero.review.exception.ReviewErrorType
import com.zerozero.review.presentation.request.ReviewRequest
import com.zerozero.user.domain.model.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@Tag(name = "Review", description = "리뷰")
@RestController
class CreateStoreReviewController(
    private val reviewService: ReviewService
) {

    @Operation(
        summary = "리뷰 등록 API",
        description = "판매점에 대한 리뷰를 등록합니다.",
        operationId = "/review"
    )
    @ApiErrorCode(
        GlobalErrorType::class, ReviewErrorType::class
    )
    @Authorization
    @PostMapping("/review")
    fun createStoreReview(
        @RequestParam @Schema(description = "판매점 ID") storeId: UUID,
        @Valid @RequestBody reviewRequest: ReviewRequest,
        @Parameter(hidden = true) @LoginUser user: User
    ): ApiResponse<Any> {
        reviewService.createStoreReview(storeId, reviewRequest, user)
        return ApiResponse.success()
    }
}
