package com.zerozero.store.presentation;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.Store;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.store.application.SearchStoreUseCase;
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
public class SearchStoreController {

  private final SearchStoreUseCase searchStoreUseCase;

  @Operation(
      summary = "판매점 검색 API",
      description = "등록할 판매점을 쿼리를 통해 검색합니다.",
      operationId = "/store/search"
  )
  @GetMapping("/store/search")
  public ResponseEntity<SearchStoreResponse> searchStore(@ParameterObject SearchStoreRequest request,
      @Parameter(hidden = true) AccessToken accessToken) {
    SearchStoreUseCase.SearchStoreResponse searchStoreResponse = searchStoreUseCase.execute(
        SearchStoreUseCase.SearchStoreRequest.builder()
            .query(request.getQuery())
            .accessToken(accessToken)
            .build());
    if (searchStoreResponse == null || !searchStoreResponse.isSuccess()) {
      Optional.ofNullable(searchStoreResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(SearchStoreResponse.builder().stores(searchStoreResponse.getStores()).build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "판매점 검색 응답")
  public static class SearchStoreResponse extends BaseResponse<GlobalErrorCode> {

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
  public static class SearchStoreRequest implements BaseRequest {

    @Schema(description = "판매점 검색 쿼리", example = "꿉당")
    private String query;
  }
}
