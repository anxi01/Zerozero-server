package com.zerozero.store.domain.response

import com.zerozero.store.domain.model.Store
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class StoreResponse(
    @Schema(description = "판매점 ID", example = "8e3006f4-3a16-11ef-9454-0242ac120002")
    val id: UUID,

    @Schema(description = "카카오 ID", example = "25770215")
    val kakaoId: String,

    @Schema(description = "판매점 이름", example = "제로 피자")
    val name: String,

    @Schema(description = "판매 종류", example = "음식점 > 한식 > 육류,고기")
    val category: String,

    @Schema(description = "전화번호", example = "02-525-6692")
    val phone: String,

    @Schema(description = "판매점 주소", example = "서울특별시 서초구 서초동")
    val address: String,

    @Schema(description = "판매점 도로명 주소", example = "서울특별시 서초구 서초대로50길 82 정원빌딩")
    val roadAddress: String,

    @Schema(description = "판매점 x좌표(경도)", example = "127.01275515884753")
    val longitude: String,

    @Schema(description = "판매점 y좌표(위도)", example = "37.49206032952165")
    val latitude: String,

    @Schema(description = "제로음료 판매 여부", example = "true")
    val status: Boolean,

    @Schema(description = "제로음료 등록 이미지 목록")
    val images: List<String>,

    @Schema(description = "판매점 상세페이지 URL", example = "http://place.map.kakao.com/25770215")
    val placeUrl: String,
) {
    companion object {

        fun from(store: Store): StoreResponse {
            return StoreResponse(
                id = store.id,
                kakaoId = store.kakaoId,
                name = store.name,
                category = store.category,
                phone = store.phone,
                address = store.address.address,
                roadAddress = store.address.roadAddress,
                longitude = store.geoLocation.longitude,
                latitude = store.geoLocation.latitude,
                status = store.status,
                images = store.images.map { it.imageUrl },
                placeUrl = store.placeUrl
            )
        }

        fun from(store: com.zerozero.store.infrastructure.mongodb.Store): StoreResponse {
            return StoreResponse(
                id = store.storeId,
                kakaoId = store.kakaoId,
                name = store.name,
                category = store.category,
                phone = store.phone,
                address = store.address,
                roadAddress = store.roadAddress,
                longitude = store.longitude,
                latitude = store.latitude,
                status = store.status,
                images = emptyList(),
                placeUrl = store.placeUrl
            )
        }

        fun of(storeSearchResponse: StoreSearchResponse, storeId: UUID?, status: Boolean): StoreResponse {
            return StoreResponse(
                id = storeId ?: UUID.randomUUID(),
                kakaoId = storeSearchResponse.id,
                name = storeSearchResponse.placeName,
                category = storeSearchResponse.categoryName,
                phone = storeSearchResponse.phone,
                address = storeSearchResponse.addressName,
                roadAddress = storeSearchResponse.roadAddressName,
                longitude = storeSearchResponse.longitude,
                latitude = storeSearchResponse.latitude,
                status = status,
                images = emptyList(),
                placeUrl = storeSearchResponse.placeUrl
            )
        }
    }
}
