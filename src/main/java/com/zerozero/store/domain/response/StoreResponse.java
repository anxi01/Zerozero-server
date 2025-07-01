package com.zerozero.store.domain.response;

import com.zerozero.image.domain.model.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record StoreResponse(

        @Schema(description = "판매점 ID", example = "8e3006f4-3a16-11ef-9454-0242ac120002")
        UUID id,

        @Schema(description = "카카오 ID", example = "25770215")
        String kakaoId,

        @Schema(description = "판매점 이름", example = "제로 피자")
        String name,

        @Schema(description = "판매 종류", example = "음식점 > 한식 > 육류,고기")
        String category,

        @Schema(description = "전화번호", example = "02-525-6692")
        String phone,

        @Schema(description = "판매점 주소", example = "서울특별시 서초구 서초동")
        String address,

        @Schema(description = "판매점 도로명 주소", example = "서울특별시 서초구 서초대로50길 82 정원빌딩")
        String roadAddress,

        @Schema(description = "판매점 x좌표(경도)", example = "127.01275515884753")
        String longitude,

        @Schema(description = "판매점 y좌표(위도)", example = "37.49206032952165")
        String latitude,

        @Schema(description = "제로음료 판매 여부", example = "true")
        boolean status,

        @Schema(description = "제로음료 등록 이미지 목록")
        List<String> images,

        @Schema(description = "판매점 상세페이지 URL", example = "http://place.map.kakao.com/25770215")
        String placeUrl
) {
    public static StoreResponse from(com.zerozero.store.domain.model.Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .kakaoId(store.getKakaoId())
                .name(store.getName())
                .category(store.getCategory())
                .phone(store.getPhone())
                .address(store.getAddress().getAddress())
                .roadAddress(store.getAddress().getRoadAddress())
                .longitude(store.getGeoLocation().getLongitude())
                .latitude(store.getGeoLocation().getLatitude())
                .status(store.getStatus())
                .images(store.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList()))
                .placeUrl(store.getPlaceUrl())
                .build();
    }

    public static StoreResponse from(com.zerozero.store.infrastructure.mongodb.Store store) {
        return StoreResponse.builder()
                .id(store.getStoreId())
                .kakaoId(store.getKakaoId())
                .name(store.getName())
                .category(store.getCategory())
                .phone(store.getPhone())
                .address(store.getAddress())
                .roadAddress(store.getRoadAddress())
                .longitude(store.getLongitude())
                .latitude(store.getLatitude())
                .status(store.isStatus())
                .placeUrl(store.getPlaceUrl())
                .build();
    }

    public static StoreResponse of(StoreSearchResponse storeSearchResponse, UUID storeId, boolean status) {
        return StoreResponse.builder()
                .id(storeId)
                .kakaoId(storeSearchResponse.id())
                .name(storeSearchResponse.placeName())
                .category(storeSearchResponse.categoryName())
                .phone(storeSearchResponse.phone())
                .address(storeSearchResponse.addressName())
                .roadAddress(storeSearchResponse.roadAddressName())
                .longitude(storeSearchResponse.longitude())
                .latitude(storeSearchResponse.latitude())
                .status(status)
                .placeUrl(storeSearchResponse.placeUrl())
                .build();
    }

}
