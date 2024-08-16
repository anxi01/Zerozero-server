package com.zerozero.store.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.Store;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.StoreJPARepository;
import com.zerozero.core.domain.infra.repository.UserJPARepository;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.external.kakao.search.application.RequestKakaoKeywordSearchUseCase;
import com.zerozero.external.kakao.search.application.RequestKakaoKeywordSearchUseCase.RequestKakaoKeywordSearchRequest;
import com.zerozero.external.kakao.search.dto.KeywordSearchResponse;
import com.zerozero.external.kakao.search.dto.KeywordSearchResponse.Document;
import com.zerozero.store.application.SearchNearbyStoresUseCase.SearchNearbyStoresRequest;
import com.zerozero.store.application.SearchNearbyStoresUseCase.SearchNearbyStoresResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchNearbyStoresUseCase implements BaseUseCase<SearchNearbyStoresRequest, SearchNearbyStoresResponse> {

  private final JwtUtil jwtUtil;

  private final RequestKakaoKeywordSearchUseCase requestKakaoKeywordSearchUseCase;

  private final UserJPARepository userJPARepository;

  private final StoreJPARepository storeJPARepository;

  private static final Integer DEFAULT_RADIUS = 2000;

  @Override
  public SearchNearbyStoresResponse execute(SearchNearbyStoresRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[SearchNearbyStoresUseCase] Invalid request");
      return SearchNearbyStoresResponse.builder()
          .success(false)
          .errorCode(SearchNearbyStoresErrorCode.NOT_EXIST_SEARCH_CONDITION)
          .build();
    }
    AccessToken accessToken = request.getAccessToken();
    if (jwtUtil.isTokenExpired(accessToken.getToken())) {
      log.error("[SearchNearbyStoresUseCase] Expired access token");
      return SearchNearbyStoresResponse.builder()
          .success(false)
          .errorCode(SearchNearbyStoresErrorCode.EXPIRED_TOKEN)
          .build();
    }
    String userEmail = jwtUtil.extractUsername(accessToken.getToken());
    User user = userJPARepository.findByEmail(userEmail);
    if (user == null) {
      log.error("[SearchNearbyStoresUseCase] not found user with email {}", userEmail);
      return SearchNearbyStoresResponse.builder()
          .success(false)
          .errorCode(SearchNearbyStoresErrorCode.NOT_EXIST_USER)
          .build();
    }
    KeywordSearchResponse keywordSearchResponse = requestKakaoKeywordSearchUseCase.execute(
            RequestKakaoKeywordSearchRequest.builder()
                .query(request.getQuery())
                .longitude(String.valueOf(request.getLongitude()))
                .latitude(String.valueOf(request.getLatitude()))
                .radius(DEFAULT_RADIUS)
                .build())
        .getKeywordSearchResponse();
    if (keywordSearchResponse == null || keywordSearchResponse.getMeta().getTotalCount() == 0) {
      log.error("[SearchNearbyStoresUseCase] Search response is null");
      return SearchNearbyStoresResponse.builder()
          .success(false)
          .errorCode(SearchNearbyStoresErrorCode.NOT_EXIST_SEARCH_RESPONSE)
          .build();
    }
    List<Document> filteredItems = Arrays.stream(keywordSearchResponse.getDocuments()).peek(item -> {
      boolean isSelling = storeJPARepository.existsByNameAndLongitudeAndLatitudeAndStatusIsTrue(
          item.getPlaceName(), item.getX(), item.getY());
      if (isSelling) {
        Store store = storeJPARepository.findByNameAndLongitudeAndLatitudeAndStatusIsTrue(item.getPlaceName(),
            item.getX(), item.getY());
        item.setStatus(true);
        item.setStoreId(store.getId());
        item.setImages(store.getImages());
      }
    }).collect(Collectors.toList());
    return SearchNearbyStoresResponse.builder()
        .stores(filteredItems.stream()
            .map(com.zerozero.core.domain.vo.Store::of)
            .collect(Collectors.toList()))
        .build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum SearchNearbyStoresErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_SEARCH_CONDITION(HttpStatus.BAD_REQUEST, "검색 조건이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    NOT_EXIST_SEARCH_RESPONSE(HttpStatus.BAD_REQUEST, "검색 응답이 존재하지 않습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
      return new DomainException(httpStatus, this);
    }
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class SearchNearbyStoresResponse extends BaseResponse<SearchNearbyStoresErrorCode> {

    private List<com.zerozero.core.domain.vo.Store> stores;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class SearchNearbyStoresRequest implements BaseRequest {

    private String query;

    private Double longitude;

    private Double latitude;

    private AccessToken accessToken;

    @Override
    public boolean isValid() {
      return query != null && longitude != null && latitude != null && accessToken != null;
    }
  }

}
