package com.zerozero.review.presentation;

import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.review.application.DeleteStoreReviewUseCase;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰")
public class DeleteStoreReviewController {

  private final DeleteStoreReviewUseCase deleteStoreReviewUseCase;

  @Operation(
      summary = "리뷰 삭제 API",
      description = "리뷰 ID를 통해 리뷰를 삭제합니다.",
      operationId = "/review/{reviewId}"
  )
  @DeleteMapping("/review/{reviewId}")
  public ResponseEntity<DeleteStoreReviewResponse> deleteStoreReview(@PathVariable(name = "reviewId") @Schema(description = "리뷰 ID") UUID reviewId,
      @Parameter(hidden = true) AccessToken accessToken) {
    DeleteStoreReviewUseCase.DeleteStoreReviewResponse deleteStoreReviewResponse = deleteStoreReviewUseCase.execute(
        DeleteStoreReviewUseCase.DeleteStoreReviewRequest.builder()
            .reviewId(reviewId)
            .accessToken(accessToken)
            .build());
    if (deleteStoreReviewResponse == null || !deleteStoreReviewResponse.isSuccess()) {
      Optional.ofNullable(deleteStoreReviewResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(DeleteStoreReviewResponse.builder().build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "리뷰 삭제 응답")
  public static class DeleteStoreReviewResponse extends BaseResponse<GlobalErrorCode> {
  }

}
