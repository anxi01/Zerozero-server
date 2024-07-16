package com.zerozero.store.presentation;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.entity.Review.Filter;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.Store;
import com.zerozero.core.domain.vo.User;
import com.zerozero.core.domain.vo.ZeroDrink;
import com.zerozero.core.domain.vo.ZeroDrink.Type;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.review.application.ReadStoreReviewUseCase;
import com.zerozero.review.application.ReadStoreReviewUseCase.ReadStoreReviewRequest;
import com.zerozero.review.application.ReadStoreReviewUseCase.ReadStoreReviewResponse;
import com.zerozero.store.application.ReadStoreInfoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Store", description = "판매점")
public class ReadStoreInfoController {

  private final ReadStoreInfoUseCase readStoreInfoUseCase;

  private final ReadStoreReviewUseCase readStoreReviewUseCase;

  @Operation(
      summary = "판매점 조회 API",
      description = "판매점 ID를 통해 판매점과 리뷰를 조회합니다.",
      operationId = "/store"
  )
  @GetMapping("/store")
  public ResponseEntity<ReadStoreInfoResponse> readStoreInfo(@ParameterObject ReadStoreInfoRequest request,
      @Parameter(hidden = true) AccessToken accessToken) {
    ReadStoreInfoUseCase.ReadStoreInfoResponse readStoreInfoResponse = readStoreInfoUseCase.execute(
        ReadStoreInfoUseCase.ReadStoreInfoRequest.builder()
            .storeId(request.getStoreId())
            .accessToken(accessToken)
            .build());
    if (readStoreInfoResponse == null || !readStoreInfoResponse.isSuccess()) {
      Optional.ofNullable(readStoreInfoResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    ReadStoreReviewResponse readStoreReviewResponse = readStoreReviewUseCase.execute(ReadStoreReviewRequest.builder()
        .storeId(request.getStoreId())
        .filter(request.getFilter())
        .build());
    if (readStoreReviewResponse == null || !readStoreReviewResponse.isSuccess()) {
      Optional.ofNullable(readStoreReviewResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(toResponse(readStoreInfoResponse, readStoreReviewResponse));
  }

  private ReadStoreInfoResponse toResponse(ReadStoreInfoUseCase.ReadStoreInfoResponse readStoreInfoResponse, ReadStoreReviewResponse readStoreReviewResponse) {
    return ReadStoreInfoResponse.builder()
        .store(readStoreInfoResponse.getStore())
        .reviews(Arrays.stream(readStoreReviewResponse.getReviews())
            .map(ReadStoreInfoResponse.Review::of).toArray(
                ReadStoreInfoResponse.Review[]::new))
        .zeroDrinks(getTop3ZeroDrinks(readStoreReviewResponse.getReviews()))
        .build();
  }

  private ZeroDrink[] getTop3ZeroDrinks(ReadStoreReviewResponse.Review[] reviews) {
    ZeroDrink[] allZeroDrinks = Arrays.stream(reviews).flatMap(review -> Arrays.stream(review.getReview().getZeroDrinks())).toArray(ZeroDrink[]::new);
    return Arrays.stream(allZeroDrinks)
        .collect(Collectors.groupingBy(ZeroDrink::getType, Collectors.counting()))
        .entrySet().stream()
        .sorted(Map.Entry.<Type, Long>comparingByValue().reversed())
        .limit(3)
        .map(Map.Entry::getKey)
        .flatMap(type -> Arrays.stream(allZeroDrinks).filter(drink -> drink.getType() == type))
        .toArray(ZeroDrink[]::new);
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "판매점 조회 응답")
  public static class ReadStoreInfoResponse extends BaseResponse<GlobalErrorCode> {

    @Schema(description = "판매점 조회 정보")
    private Store store;

    @Schema(description = "리뷰 목록")
    private Review[] reviews;

    @Schema(description = "제로 음료수 순위")
    private ZeroDrink[] zeroDrinks;

    record Review(com.zerozero.core.domain.vo.Review review, User user) {

      public static Review of(ReadStoreReviewResponse.Review review) {
        return new Review(review.getReview(), review.getUser());
      }
    }
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "판매점 조회 요청")
  public static class ReadStoreInfoRequest implements BaseRequest {

    @Schema(description = "판매점 ID", example = "11ef3e05-f45b-7e6c-a084-7b554bfaa162")
    private UUID storeId;

    @Schema(description = "리뷰 정렬 조건 : RECENT(최신순), RECOMMEND(추천순)", example = "RECOMMEND")
    private Filter filter;
  }
}
