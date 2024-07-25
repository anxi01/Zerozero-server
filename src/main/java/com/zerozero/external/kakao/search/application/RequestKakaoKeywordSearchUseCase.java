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
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
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

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", KAKAO_AUTHORIZATION_PREFIX + kakaoProperty.getRestApiKey());
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity httpEntity = new HttpEntity<>(headers);
    ParameterizedTypeReference<KeywordSearchResponse> responseType = new ParameterizedTypeReference<>() {};
    ResponseEntity<KeywordSearchResponse> responseEntity = new RestTemplate().exchange(uri, HttpMethod.GET, httpEntity, responseType);
    return RequestKakaoKeywordSearchResponse.builder().keywordSearchResponse(responseEntity.getBody()).build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum RequestKakaoKeywordSearchErrorCode implements BaseErrorCode<DomainException> {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 값이 유효하지 않습니다.");

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

      queryParams.add("query", query);
      queryParams.add("category_group_code",
          Optional.ofNullable(categoryGroupCode).map(CategoryGroupCode::toString).orElse(null));
      queryParams.add("x", longitude);
      queryParams.add("y", latitude);
      queryParams.add("radius", Optional.ofNullable(radius).map(String::valueOf).orElse(null));
      queryParams.add("rect", rect);
      queryParams.add("page", Optional.ofNullable(page).map(String::valueOf).orElse(null));
      queryParams.add("size", Optional.ofNullable(size).map(String::valueOf).orElse(null));
      queryParams.add("sort", sort);

      return queryParams;
    }

    @Override
    public boolean isValid() {
      return query != null && !query.isEmpty();
    }
  }
}
