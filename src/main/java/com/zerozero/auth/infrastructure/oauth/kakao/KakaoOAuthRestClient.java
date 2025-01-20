package com.zerozero.auth.infrastructure.oauth.kakao;

import com.zerozero.auth.exception.AuthenticationErrorCode;
import com.zerozero.auth.infrastructure.oauth.OAuthRestClient;
import com.zerozero.auth.infrastructure.oauth.common.OAuthAccessTokenResponse;
import com.zerozero.auth.infrastructure.oauth.common.OAuthResourceResponse;
import com.zerozero.auth.infrastructure.oauth.kakao.dto.KakaoResourceResponse;
import com.zerozero.configuration.property.oauth.KakaoOAuthProperty;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Log4j2
public class KakaoOAuthRestClient implements OAuthRestClient {

  private static final String GRANT_TYPE = "authorization_code";

  private final KakaoOAuthProperty kakaoOAuthProperty;

  @Override
  public URI getAuthUrl() {
    return UriComponentsBuilder.fromUriString(kakaoOAuthProperty.getAuthUri())
        .queryParam("client_id", kakaoOAuthProperty.getClientId())
        .queryParam("redirect_uri", kakaoOAuthProperty.getRedirectUri())
        .queryParam("response_type", "code")
        .queryParam("scope", kakaoOAuthProperty.getScope())
        .build()
        .toUri();
  }

  @Override
  public OAuthAccessTokenResponse getAccessToken(String authCode) {
    if (authCode == null || authCode.isEmpty()) {
      log.error("[KakaoOAuthRestClient] authCode is null");
      throw AuthenticationErrorCode.NOT_EXIST_AUTH_CODE.toException();
    }
    try {
      return RestClient.create()
          .post()
          .uri(kakaoOAuthProperty.getTokenUri())
          .headers(header -> {
            header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
          })
          .body(createAccessTokenRequestBody(authCode))
          .retrieve()
          .body(OAuthAccessTokenResponse.class);
    } catch (Exception e) {
      log.error("[KakaoOAuthRestClient] error", e);
      throw AuthenticationErrorCode.ACCESS_TOKEN_NOT_ISSUED.toException();
    }
  }

  @Override
  public OAuthResourceResponse getResource(String accessToken) {
    if (accessToken == null || accessToken.isEmpty()) {
      log.error("[KakaoOAuthRestClient] accessToken is null");
      throw AuthenticationErrorCode.ACCESS_TOKEN_NOT_ISSUED.toException();
    }
    try {
      KakaoResourceResponse kakaoResourceResponse = RestClient.create()
          .get()
          .uri(kakaoOAuthProperty.getResourceUri())
          .headers(header -> {
            header.setBearerAuth(accessToken);
            header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
          })
          .retrieve()
          .body(KakaoResourceResponse.class);
      long id = Optional.ofNullable(kakaoResourceResponse)
          .map(KakaoResourceResponse::id)
          .orElseThrow(AuthenticationErrorCode.NOT_EXIST_RESOURCE_RESPONSE::toException);
      KakaoResourceResponse.Response kakaoAccount = Optional.ofNullable(
              kakaoResourceResponse.kakaoAccount())
          .orElseThrow(AuthenticationErrorCode.NOT_EXIST_RESOURCE_RESPONSE::toException);
      String email = Optional.ofNullable(kakaoAccount.email())
          .orElseThrow(AuthenticationErrorCode.NOT_EXIST_RESOURCE_RESPONSE::toException);
      return OAuthResourceResponse.builder()
          .id(String.valueOf(id))
          .email(email)
          .build();
    } catch (Exception e) {
      log.error("[KakaoOAuthRestClient] error", e);
      throw AuthenticationErrorCode.RESOURCE_SERVER_UNAVAILABLE.toException();
    }
  }

  private MultiValueMap<String, String> createAccessTokenRequestBody(String authCode) {
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add("client_id", kakaoOAuthProperty.getClientId());
    parameters.add("client_secret", kakaoOAuthProperty.getClientSecret());
    parameters.add("code", authCode);
    parameters.add("grant_type", GRANT_TYPE);
    parameters.add("redirect_uri", kakaoOAuthProperty.getRedirectUri());
    return parameters;
  }
}
