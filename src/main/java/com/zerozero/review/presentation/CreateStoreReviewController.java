package com.zerozero.review.presentation;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.ZeroDrink;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.review.application.CreateStoreReviewUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰")
public class CreateStoreReviewController {

  private final CreateStoreReviewUseCase createStoreReviewUseCase;

  @Operation(
      summary = "리뷰 등록 API",
      description = "판매점에 대한 리뷰를 등록합니다.",
      operationId = "/review"
  )
  @PostMapping("/review")
  public ResponseEntity<CreateStoreReviewResponse> createStoreReview(@RequestParam @Schema(description = "판매점 ID") UUID storeId,
      @RequestBody CreateStoreReviewRequest request,
      @Parameter(hidden = true) AccessToken accessToken) {
    CreateStoreReviewUseCase.CreateStoreReviewResponse createStoreReviewResponse = createStoreReviewUseCase.execute(
        CreateStoreReviewUseCase.CreateStoreReviewRequest.builder()
            .storeId(storeId)
            .content(request.getContent())
            .zeroDrinks(request.getZeroDrinks())
            .accessToken(accessToken)
            .build());
    if (createStoreReviewResponse == null || !createStoreReviewResponse.isSuccess()) {
      Optional.ofNullable(createStoreReviewResponse)
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
  @Schema(description = "리뷰 등록 응답")
  public static class CreateStoreReviewResponse extends BaseResponse<GlobalErrorCode> {
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "리뷰 등록 요청")
  public static class CreateStoreReviewRequest implements BaseRequest {

    @Schema(description = "리뷰 내용", example = "제로콜라 판매 중!")
    private String content;

    @Schema(description = "제로 음료수 목록", example = "[\n" +
        "    {\n" +
        "      \"type\": \"COCA_COLA_ZERO\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"PEPSI_ZERO\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"SPRITE_ZERO\"\n" +
        "    }\n" +
        "]")
    private ZeroDrink[] zeroDrinks;
  }
}
