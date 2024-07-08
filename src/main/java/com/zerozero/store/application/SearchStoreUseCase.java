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
import com.zerozero.external.naver.search.application.SearchNaverLocalUseCase;
import com.zerozero.external.naver.search.application.SearchNaverLocalUseCase.SearchNaverLocalRequest;
import com.zerozero.external.naver.search.dto.SearchLocalResponse;
import com.zerozero.external.naver.search.dto.SearchLocalResponse.SearchLocalItem;
import com.zerozero.store.application.SearchStoreUseCase.SearchStoreRequest;
import com.zerozero.store.application.SearchStoreUseCase.SearchStoreResponse;
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
public class SearchStoreUseCase implements BaseUseCase<SearchStoreRequest, SearchStoreResponse> {

  private final JwtUtil jwtUtil;

  private final SearchNaverLocalUseCase searchNaverLocalUseCase;

  private final UserJPARepository userJPARepository;

  private final StoreJPARepository storeJPARepository;

  @Override
  public SearchStoreResponse execute(SearchStoreRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[SearchStoreUseCase] Invalid request");
      return SearchStoreResponse.builder()
          .success(false)
          .errorCode(SearchStoreErrorCode.NOT_EXIST_SEARCH_CONDITION)
          .build();
    }
    AccessToken accessToken = request.getAccessToken();
    if (jwtUtil.isTokenExpired(accessToken.getToken())) {
      log.error("[SearchStoreUseCase] Expired access token");
      return SearchStoreResponse.builder().success(false)
          .errorCode(SearchStoreErrorCode.EXPIRED_TOKEN)
          .build();
    }
    String userEmail = jwtUtil.extractUsername(accessToken.getToken());
    User user = userJPARepository.findByEmail(userEmail);
    if (user == null) {
      log.error("[SearchStoreUseCase] not found user with email {}", userEmail);
      return SearchStoreResponse.builder().success(false)
          .errorCode(SearchStoreErrorCode.NOT_EXIST_USER)
          .build();
    }
    SearchLocalResponse searchLocalResponse = searchNaverLocalUseCase.execute(
        SearchNaverLocalRequest.builder().query(request.getQuery()).build()).getResponse();
    if (searchLocalResponse == null || searchLocalResponse.getTotal() == 0) {
      log.error("[SearchStoreUseCase] Search response is null");
      return SearchStoreResponse.builder()
          .success(false)
          .errorCode(SearchStoreErrorCode.NOT_EXIST_SEARCH_RESPONSE)
          .build();
    }
    List<SearchLocalItem> searchLocalItems = searchLocalResponse.getItems();
    List<SearchLocalItem> filteredItems = searchLocalItems.stream().peek(item -> {
      boolean isSelling = storeJPARepository.existsByNameAndMapxAndMapyAndStatusIsTrue(
          item.getTitle(), item.getMapx(), item.getMapy());
      if (isSelling) {
        item.setStatus(true);
        Store store = storeJPARepository.findByNameAndMapxAndMapyAndStatusIsTrue(item.getTitle(),
            item.getMapx(), item.getMapy());
        item.setStoreId(store.getId());
      }
    }).collect(Collectors.toList());
    return SearchStoreResponse.builder().stores(filteredItems.stream()
        .map(com.zerozero.core.domain.vo.Store::of)
        .collect(Collectors.toList())).build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum SearchStoreErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_SEARCH_CONDITION(HttpStatus.BAD_REQUEST, "검색 조건이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
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
  public static class SearchStoreResponse extends BaseResponse<SearchStoreErrorCode> {

    private List<com.zerozero.core.domain.vo.Store> stores;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class SearchStoreRequest implements BaseRequest {

    private String query;

    private AccessToken accessToken;

    @Override
    public boolean isValid() {
      return query != null && !query.isEmpty() && accessToken != null;
    }
  }

}
