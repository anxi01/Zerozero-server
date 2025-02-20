package com.zerozero.store.infrastructure.kakao.search.application;

import com.zerozero.store.domain.response.StoreSearchResponse;
import com.zerozero.store.domain.service.StoreSearcher;
import com.zerozero.store.exception.StoreErrorType;
import com.zerozero.store.exception.StoreException;
import com.zerozero.store.infrastructure.kakao.search.request.KakaoSearchRequest;
import com.zerozero.store.infrastructure.kakao.search.response.KakaoSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class KakaoStoreSearcher implements StoreSearcher {

    private final KakaoSearchService kakaoSearchService;
    private static final Integer DEFAULT_RADIUS = 2000;

    public List<StoreSearchResponse> search(String query) {
        KakaoSearchResponse kakaoSearchResponse = kakaoSearchService.search(
                KakaoSearchRequest.builder()
                        .query(query)
                        .build());

        if (kakaoSearchResponse == null || kakaoSearchResponse.getMeta().getTotalCount() == 0) {
            log.error("[KakaoStoreSearcher] Search response is null or empty");
            throw new StoreException(StoreErrorType.NOT_EXIST_SEARCH_RESPONSE);
        }

        return Arrays.stream(kakaoSearchResponse.getDocuments())
                .map(StoreSearchResponse::from)
                .collect(Collectors.toList());
    }

    public List<StoreSearchResponse> searchByLocation(String query, double longitude, double latitude) {
        KakaoSearchResponse kakaoSearchResponse = kakaoSearchService.search(
                KakaoSearchRequest.builder()
                        .query(query)
                        .longitude(String.valueOf(longitude))
                        .latitude(String.valueOf(latitude))
                        .radius(DEFAULT_RADIUS)
                        .build());

        if (kakaoSearchResponse == null || kakaoSearchResponse.getMeta().getTotalCount() == 0) {
            log.error("[KakaoStoreSearcher] Location-based search response is null or empty");
            throw new StoreException(StoreErrorType.NOT_EXIST_SEARCH_RESPONSE);
        }

        return Arrays.stream(kakaoSearchResponse.getDocuments())
                .map(StoreSearchResponse::from)
                .collect(Collectors.toList());
    }

}
