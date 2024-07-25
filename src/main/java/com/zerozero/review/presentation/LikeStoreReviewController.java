package com.zerozero.review.presentation;

import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.review.application.LikeStoreReviewUseCase;
import com.zerozero.review.application.LikeStoreReviewUseCase.LikeStoreReviewErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
  @ApiErrorCode({GlobalErrorCode.class, LikeStoreReviewErrorCode.class})
  @PatchMapping("/review/like/{reviewId}")
  public ResponseEntity<LikeStoreReviewResponse> likeStoreReview(@PathVariable(name = "reviewId") @Schema(description = "리뷰 ID") UUID reviewId,
      @Parameter(hidden = true) AccessToken accessToken) {
    LikeStoreReviewUseCase.LikeStoreReviewResponse likeStoreReviewResponse = likeStoreReviewUseCase.execute(
        LikeStoreReviewUseCase.LikeStoreReviewRequest.builder()
            .reviewId(reviewId)
            .accessToken(accessToken)
            .build());
    if (likeStoreReviewResponse == null || !likeStoreReviewResponse.isSuccess()) {
      Optional.ofNullable(likeStoreReviewResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok().build();
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "리뷰 좋아요 응답")
  public static class LikeStoreReviewResponse extends BaseResponse<GlobalErrorCode> {
  }

}
