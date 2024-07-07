package com.zerozero.external.naver.search.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.external.naver.search.application.SearchNaverLocalUseCase.SearchNaverLocalRequest;
import com.zerozero.external.naver.search.application.SearchNaverLocalUseCase.SearchNaverLocalResponse;
import com.zerozero.external.naver.search.core.configuration.NaverClient;
import com.zerozero.external.naver.search.dto.SearchLocalRequest;
import com.zerozero.external.naver.search.dto.SearchLocalResponse;
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

@Log4j2
@Service
@RequiredArgsConstructor
public class SearchNaverLocalUseCase implements BaseUseCase<SearchNaverLocalRequest, SearchNaverLocalResponse> {

  private final NaverClient naverClient;

  @Override
  public SearchNaverLocalResponse execute(SearchNaverLocalRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[SearchNaverLocalUseCase] Search naver request is empty");
      return SearchNaverLocalResponse.builder()
          .success(false)
          .errorCode(SearchNaverLocalErrorCode.NOT_EXIST_SEARCH_CONDITION)
          .build();
    }
    SearchLocalRequest searchLocalRequest = new SearchLocalRequest();
    searchLocalRequest.setQuery(request.getQuery());
    SearchLocalResponse searchLocalResponse = naverClient.localSearch(searchLocalRequest);
    return SearchNaverLocalResponse.builder().response(searchLocalResponse).build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum SearchNaverLocalErrorCode implements BaseErrorCode<DomainException> {

    NOT_EXIST_SEARCH_CONDITION(HttpStatus.BAD_REQUEST, "지역 검색 조건이 존재하지 않습니다.");

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
  public static class SearchNaverLocalResponse extends BaseResponse<SearchNaverLocalErrorCode> {

    private SearchLocalResponse response;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class SearchNaverLocalRequest implements BaseRequest {

    private String query;

    @Override
    public boolean isValid() {
      return query != null && !query.isEmpty();
    }
  }

}
