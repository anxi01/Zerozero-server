package com.zerozero.review.presentation;

import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.ZeroDrink;
import com.zerozero.core.domain.vo.ZeroDrink.Type;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.review.application.UpdateStoreReviewUseCase;
import com.zerozero.review.application.UpdateStoreReviewUseCase.UpdateStoreReviewErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰")
public class UpdateStoreReviewController {

  private final UpdateStoreReviewUseCase updateStoreReviewUseCase;

  @Operation(
      summary = "리뷰 수정 API",
      description = "리뷰 ID를 통해 리뷰를 수정합니다.",
      operationId = "/review/{reviewId}"
  )
  @ApiErrorCode({GlobalErrorCode.class, UpdateStoreReviewErrorCode.class})
  @PatchMapping("/review/{reviewId}")
  public ResponseEntity<UpdateStoreReviewResponse> updateStoreReview(@PathVariable(name = "reviewId") @Schema(description = "리뷰 ID") UUID reviewId,
      @RequestBody UpdateStoreReviewRequest request,
      @Parameter(hidden = true) AccessToken accessToken) {
    UpdateStoreReviewUseCase.UpdateStoreReviewResponse updateStoreReviewResponse = updateStoreReviewUseCase.execute(
        UpdateStoreReviewUseCase.UpdateStoreReviewRequest.builder()
            .reviewId(reviewId)
            .content(request.getContent())
            .zeroDrinks(Optional.ofNullable(request.getZeroDrinks())
                .map(zeroDrinks -> zeroDrinks.stream().map(zeroDrink -> ZeroDrink.builder()
                        .type(zeroDrink)
                        .build())
                    .toArray(ZeroDrink[]::new))
                .orElse(null))
            .accessToken(accessToken)
            .build());
    if (updateStoreReviewResponse == null || !updateStoreReviewResponse.isSuccess()) {
      Optional.ofNullable(updateStoreReviewResponse)
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
  @Schema(description = "리뷰 수정 웅답")
  public static class UpdateStoreReviewResponse extends BaseResponse<GlobalErrorCode> {
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "리뷰 수정 요청")
  public static class UpdateStoreReviewRequest implements BaseRequest {

    @Schema(description = "리뷰 내용", example = "제로콜라 판매 중!")
    private String content;

    @Schema(description = "제로 음료수 목록", example = "[\"COCA_COLA_ZERO\", \"PEPSI_ZERO\", \"SPRITE_ZERO\"]")
    private List<Type> zeroDrinks;
  }
}
