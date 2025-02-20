package com.zerozero.auth.infrastructure.oauth.kakao;

import com.zerozero.auth.exception.AuthErrorType;
import com.zerozero.auth.exception.AuthException;
import com.zerozero.auth.application.OAuthRestClient;
import com.zerozero.auth.infrastructure.oauth.core.OAuthAccessTokenResponse;
import com.zerozero.auth.infrastructure.oauth.core.OAuthResourceResponse;
import com.zerozero.auth.infrastructure.oauth.kakao.dto.KakaoResourceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

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
            throw new AuthException(AuthErrorType.NOT_EXIST_AUTH_CODE);
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
            throw new AuthException(AuthErrorType.ACCESS_TOKEN_NOT_ISSUED);
        }
    }

    @Override
    public OAuthResourceResponse getResource(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            log.error("[KakaoOAuthRestClient] accessToken is null");
            throw new AuthException(AuthErrorType.ACCESS_TOKEN_NOT_ISSUED);
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
                    .orElseThrow(() -> new AuthException(AuthErrorType.NOT_EXIST_RESOURCE_RESPONSE));
            KakaoResourceResponse.Response kakaoAccount = Optional.ofNullable(
                            kakaoResourceResponse.kakaoAccount())
                    .orElseThrow(() -> new AuthException(AuthErrorType.NOT_EXIST_RESOURCE_RESPONSE));
            String email = Optional.ofNullable(kakaoAccount.email())
                    .orElseThrow(() -> new AuthException(AuthErrorType.NOT_EXIST_RESOURCE_RESPONSE));
            return OAuthResourceResponse.builder()
                    .id(String.valueOf(id))
                    .email(email)
                    .build();
        } catch (Exception e) {
            log.error("[KakaoOAuthRestClient] error", e);
            throw new AuthException(AuthErrorType.RESOURCE_SERVER_UNAVAILABLE);
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
