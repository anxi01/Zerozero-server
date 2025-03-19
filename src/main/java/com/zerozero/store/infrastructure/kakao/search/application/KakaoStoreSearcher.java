package com.zerozero.store.infrastructure.kakao.search.application;

import com.zerozero.store.domain.response.StoreSearchResponse;
import com.zerozero.store.domain.service.StoreSearcher;
import com.zerozero.store.exception.StoreErrorType;
import com.zerozero.store.exception.StoreException;
import com.zerozero.store.infrastructure.kakao.search.request.KakaoSearchRequest;
import com.zerozero.store.infrastructure.kakao.search.response.KakaoSearchResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class KakaoStoreSearcher implements StoreSearcher {

    private final KakaoSearchClient kakaoSearchClient;
    private static final Integer DEFAULT_RADIUS = 2000;

    @Override
    public List<StoreSearchResponse> search(String query) {
        KakaoSearchRequest request = KakaoSearchRequest.builder()
                .query(query)
                .build();
        return executeSearch(request);
    }

    @Override
    public List<StoreSearchResponse> searchByLocation(String query, double longitude, double latitude) {
        KakaoSearchRequest request = KakaoSearchRequest.builder()
                .query(query)
                .x(String.valueOf(longitude))
                .y(String.valueOf(latitude))
                .radius(DEFAULT_RADIUS)
                .build();
        return executeSearch(request);
    }

    private List<StoreSearchResponse> executeSearch(KakaoSearchRequest request) {
        KakaoSearchResponseWrapper kakaoSearchResponseWrapper = kakaoSearchClient.search(request);

        if (kakaoSearchResponseWrapper == null ||
                kakaoSearchResponseWrapper.getKakaoSearchMeta() == null ||
                kakaoSearchResponseWrapper.getKakaoSearchMeta().getTotalCount() == 0) {
            log.error("[KakaoStoreSearcher] Kakao search response is null or empty");
            throw new StoreException(StoreErrorType.NOT_EXIST_SEARCH_RESPONSE);
        }

        return Arrays.stream(kakaoSearchResponseWrapper.getKakaoSearchResponses())
                .map(StoreSearchResponse::from)
                .collect(Collectors.toList());
    }
}
