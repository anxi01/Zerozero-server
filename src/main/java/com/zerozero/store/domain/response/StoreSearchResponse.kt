package com.zerozero.store.domain.response

import com.zerozero.store.infrastructure.kakao.search.response.KakaoSearchResponse

@JvmRecord
data class StoreSearchResponse(
    val id: String,
    val placeName: String,
    val categoryName: String,
    val phone: String,
    val addressName: String,
    val roadAddressName: String,
    val longitude: String,
    val latitude: String,
    val placeUrl: String,
    val distance: String,
) {
    companion object {
        @JvmStatic
        fun from(kakaoSearchResponse: KakaoSearchResponse): StoreSearchResponse {
            return StoreSearchResponse(
                id = kakaoSearchResponse.id,
                placeName = kakaoSearchResponse.placeName,
                categoryName = kakaoSearchResponse.categoryName,
                phone = kakaoSearchResponse.phone,
                addressName = kakaoSearchResponse.addressName,
                roadAddressName = kakaoSearchResponse.roadAddressName,
                longitude = kakaoSearchResponse.x,
                latitude = kakaoSearchResponse.y,
                placeUrl = kakaoSearchResponse.placeUrl,
                distance = kakaoSearchResponse.distance
            )
        }
    }
}
