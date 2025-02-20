package com.zerozero.store.infrastructure.kakao.search.application;

import com.zerozero.store.exception.StoreErrorType;
import com.zerozero.store.exception.StoreException;
import com.zerozero.store.infrastructure.kakao.search.core.KakaoProperty;
import com.zerozero.store.infrastructure.kakao.search.request.KakaoSearchRequest;
import com.zerozero.store.infrastructure.kakao.search.response.KakaoSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Log4j2
public class KakaoSearchService {

    private final KakaoProperty kakaoProperty;
    private static final String KAKAO_AUTHORIZATION_PREFIX = "KakaoAK ";

    public KakaoSearchResponse search(KakaoSearchRequest kakaoSearchRequest) {
        URI uri = UriComponentsBuilder.fromUriString(kakaoProperty.getKeywordUrl())
                .queryParams(kakaoSearchRequest.createQueryParams())
                .build()
                .encode()
                .toUri();

        try {
            return RestClient.create()
                    .get()
                    .uri(uri)
                    .headers(header -> {
                        header.set("Authorization", KAKAO_AUTHORIZATION_PREFIX + kakaoProperty.getRestApiKey());
                        header.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .retrieve()
                    .body(KakaoSearchResponse.class);
        } catch (Exception e) {
            log.error("[KakaoSearchService] error", e);
            throw new StoreException(StoreErrorType.KAKAO_SERVICE_UNAVAILABLE);
        }
    }
}

