package com.zerozero.store.presentation;

import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.Store;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.store.application.SearchNearbyStoresUseCase;
import com.zerozero.store.application.SearchNearbyStoresUseCase.SearchNearbyStoresErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class SearchNearbyStoresController {

  private final SearchNearbyStoresUseCase searchNearbyStoresUseCase;

  @Operation(
      summary = "[메인페이지] 판매점 검색 API",
      description = "메인페이지에서 등록할 판매점을 쿼리와 경,위도를 통해 반경 2KM 내 판매점을 검색합니다.",
      operationId = "/store/search/nearby"
  )
  @ApiErrorCode({GlobalErrorCode.class, SearchNearbyStoresErrorCode.class})
  @GetMapping("/store/search/nearby")
  public ResponseEntity<SearchNearbyStoresResponse> searchNearbyStores(@ParameterObject SearchNearbyStoresRequest request,
      @Parameter(hidden = true) AccessToken accessToken) {
    SearchNearbyStoresUseCase.SearchNearbyStoresResponse searchNearbyStoresResponse = searchNearbyStoresUseCase.execute(
        SearchNearbyStoresUseCase.SearchNearbyStoresRequest.builder()
            .query(request.getQuery())
            .longitude(request.getLongitude())
            .latitude(request.getLatitude())
            .accessToken(accessToken)
            .build());
    if (searchNearbyStoresResponse == null || !searchNearbyStoresResponse.isSuccess()) {
      Optional.ofNullable(searchNearbyStoresResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(SearchNearbyStoresResponse.builder().stores(searchNearbyStoresResponse.getStores()).build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "판매점 검색 응답")
  public static class SearchNearbyStoresResponse extends BaseResponse<GlobalErrorCode> {

    @Schema(description = "판매점 목록")
    private List<Store> stores;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "판매점 검색 요청")
  public static class SearchNearbyStoresRequest implements BaseRequest {

    @Schema(description = "판매점 검색 쿼리", example = "꿉당")
    private String query;

    @Schema(description = "사용자 경도", example = "127.01727639915623")
    private Double longitude;

    @Schema(description = "사용자 위도", example = "37.4839596934158")
    private Double latitude;
  }
}
