package com.zerozero.core.domain.record;

import com.zerozero.core.domain.vo.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record Store(
    @Schema(description = "판매점 ID", example = "8e3006f4-3a16-11ef-9454-0242ac120002") UUID id,
    @Schema(description = "카카오 ID", example = "25770215") String kakaoId,
    @Schema(description = "판매점 이름", example = "제로 피자") String name,
    @Schema(description = "판매 종류", example = "음식점 > 한식 > 육류,고기") String category,
    @Schema(description = "전화번호", example = "02-525-6692") String phone,
    @Schema(description = "판매점 주소", example = "서울특별시 서초구 서초동") String address,
    @Schema(description = "판매점 도로명 주소", example = "서울특별시 서초구 서초대로50길 82 정원빌딩") String roadAddress,
    @Schema(description = "판매점 x좌표(경도)", example = "127.01275515884753") String longitude,
    @Schema(description = "판매점 y좌표(위도)", example = "37.49206032952165") String latitude,
    @Schema(description = "제로음료 판매 여부", example = "true") Boolean status,
    @Schema(description = "제로음료 등록 이미지 목록") Image[] images,
    @Schema(description = "판매점 상세페이지 URL", example = "http://place.map.kakao.com/25770215") String placeUrl) {

  public static Store of(com.zerozero.core.domain.vo.Store store) {
    return new Store(
        store.getId(),
        store.getKakaoId(),
        store.getName(),
        store.getCategory(),
        store.getPhone(),
        store.getAddress(),
        store.getRoadAddress(),
        store.getLongitude(),
        store.getLatitude(),
        store.getStatus(),
        store.getImages(),
        store.getPlaceUrl()
    );
  }
}