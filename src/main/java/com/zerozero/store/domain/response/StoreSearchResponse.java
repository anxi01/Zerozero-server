package com.zerozero.store.domain.response;

import com.zerozero.store.infrastructure.kakao.search.response.KakaoSearchResponseWrapper.KakaoSearchResponse;

public record StoreSearchResponse(
        String id,
        String placeName,
        String categoryName,
        String phone,
        String addressName,
        String roadAddressName,
        String longitude,
        String latitude,
        String placeUrl,
        String distance
) {
    public static StoreSearchResponse from(KakaoSearchResponse kakaoSearchResponse) {
        return new StoreSearchResponse(
                kakaoSearchResponse.getId(),
                kakaoSearchResponse.getPlaceName(),
                kakaoSearchResponse.getCategoryName(),
                kakaoSearchResponse.getPhone(),
                kakaoSearchResponse.getAddressName(),
                kakaoSearchResponse.getRoadAddressName(),
                kakaoSearchResponse.getX(),
                kakaoSearchResponse.getY(),
                kakaoSearchResponse.getPlaceUrl(),
                kakaoSearchResponse.getDistance()
        );
    }
}
