package com.zerozero.external.kakao.search.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.external.kakao.search.application.RequestKakaoKeywordSearchUseCase.RequestKakaoKeywordSearchRequest;
import com.zerozero.external.kakao.search.application.RequestKakaoKeywordSearchUseCase.RequestKakaoKeywordSearchResponse;
import com.zerozero.external.kakao.search.core.configuration.KakaoProperty;
import com.zerozero.external.kakao.search.dto.CategoryGroupCode;
import com.zerozero.external.kakao.search.dto.KeywordSearchResponse;
import java.net.URI;
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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Service
@RequiredArgsConstructor
public class RequestKakaoKeywordSearchUseCase implements BaseUseCase<RequestKakaoKeywordSearchRequest, RequestKakaoKeywordSearchResponse> {

  private final KakaoProperty kakaoProperty;

  @Override
  public RequestKakaoKeywordSearchResponse execute(RequestKakaoKeywordSearchRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[RequestKakaoKeywordSearchUseCase] RequestKakaoKeywordSearchRequest is invalid.");
      return RequestKakaoKeywordSearchResponse.builder()
          .success(false)
          .errorCode(RequestKakaoKeywordSearchErrorCode.INVALID_REQUEST)
          .build();
    }
    final String KAKAO_AUTHORIZATION_PREFIX = "KakaoAK ";

    URI uri = UriComponentsBuilder.fromUriString(kakaoProperty.getKeywordUrl())
        .queryParams(request.createQueryParams())
        .build()
        .encode()
        .toUri();

    try {
      KeywordSearchResponse keywordSearchResponse = RestClient.create()
          .get()
          .uri(uri)
          .headers(header -> {
            header.set("Authorization", KAKAO_AUTHORIZATION_PREFIX + kakaoProperty.getRestApiKey());
            header.setContentType(MediaType.APPLICATION_JSON);
          })
          .retrieve()
          .body(KeywordSearchResponse.class);
      return RequestKakaoKeywordSearchResponse.builder()
          .keywordSearchResponse(keywordSearchResponse).build();
    } catch (Exception e) {
      throw RequestKakaoKeywordSearchErrorCode.KAKAO_SERVICE_UNAVAILABLE.toException();
    }
  }

  @Getter
  @RequiredArgsConstructor
  public enum RequestKakaoKeywordSearchErrorCode implements BaseErrorCode<DomainException> {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 값이 유효하지 않습니다."),
    KAKAO_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "KAKAO REST API가 동작하지 않습니다."),
    ;

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
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RequestKakaoKeywordSearchResponse extends BaseResponse<RequestKakaoKeywordSearchErrorCode> {

    private KeywordSearchResponse keywordSearchResponse;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RequestKakaoKeywordSearchRequest implements BaseRequest {

    private String query;

    private CategoryGroupCode categoryGroupCode;

    private String longitude;

    private String latitude;

    private Integer radius;

    private String rect;

    private Integer page;

    private Integer size;

    private String sort;

    public MultiValueMap<String, String> createQueryParams() {
      LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

      if (query != null && !query.isEmpty()) {
        queryParams.add("query", query);
      }
      if (categoryGroupCode != null) {
        queryParams.add("category_group_code", categoryGroupCode.name());
      }
      if (longitude != null && !longitude.isEmpty()) {
        queryParams.add("x", longitude);
      }
      if (latitude != null && !latitude.isEmpty()) {
        queryParams.add("y", latitude);
      }
      if (radius != null) {
        queryParams.add("radius", String.valueOf(radius));
      }
      if (rect != null && !rect.isEmpty()) {
        queryParams.add("rect", rect);
      }
      if (page != null) {
        queryParams.add("page", String.valueOf(page));
      }
      if (size != null) {
        queryParams.add("size", String.valueOf(size));
      }
      if (sort != null && !sort.isEmpty()) {
        queryParams.add("sort", sort);
      }

      return queryParams;
    }

    @Override
    public boolean isValid() {
      return query != null && !query.isEmpty();
    }
  }
}
