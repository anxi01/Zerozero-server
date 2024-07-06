package com.zerozero.external.naver.core.configuration;

import com.zerozero.external.naver.SearchLocalRequest;
import com.zerozero.external.naver.SearchLocalResponse;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NaverClient {

  @Value("${naver.client.id}")
  private String naverClientId;

  @Value("${naver.client.secret}")
  private String naverSecret;

  @Value("${naver.url.search.local}")
  private String naverLocalSearchUrl;

  public SearchLocalResponse localSearch(SearchLocalRequest searchLocalRequest) {

    URI uri = UriComponentsBuilder.fromUriString(naverLocalSearchUrl)
        .queryParams(searchLocalRequest.toMultiValueMap())
        .build()
        .encode()
        .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Naver-Client-Id", naverClientId);
    headers.set("X-Naver-Client-Secret", naverSecret);
    headers.setContentType(MediaType.APPLICATION_JSON);

    var httpEntity = new HttpEntity<>(headers);
    var responseType = new ParameterizedTypeReference<SearchLocalResponse>(){};

    var responseEntity = new RestTemplate().exchange(uri, HttpMethod.GET, httpEntity, responseType);

    return responseEntity.getBody();
  }
}
