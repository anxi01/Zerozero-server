package com.zerozero.store.domain.response;

import com.zerozero.store.infrastructure.kakao.search.response.KakaoSearchResponse.Document;

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
    public static StoreSearchResponse from(Document document) {
        return new StoreSearchResponse(
                document.getId(),
                document.getPlaceName(),
                document.getCategoryName(),
                document.getPhone(),
                document.getAddressName(),
                document.getRoadAddressName(),
                document.getX(),
                document.getY(),
                document.getPlaceUrl(),
                document.getDistance()
        );
    }
}
