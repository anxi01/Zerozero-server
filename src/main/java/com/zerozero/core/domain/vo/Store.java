package com.zerozero.core.domain.vo;

import com.zerozero.core.domain.shared.ValueObject;
import com.zerozero.external.kakao.search.dto.KeywordSearchResponse.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "판매점")
public class Store extends ValueObject implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "판매점 ID", example = "8e3006f4-3a16-11ef-9454-0242ac120002")
  private UUID id;

  @Schema(description = "카카오 ID", example = "25770215")
  private String kakaoId;

  @Schema(description = "판매점 이름", example = "제로 피자")
  private String name;

  @Schema(description = "판매 종류", example = "음식점 > 한식 > 육류,고기")
  private String category;

  @Schema(description = "전화번호", example = "02-525-6692")
  private String phone;

  @Schema(description = "판매점 주소", example = "서울특별시 서초구 서초동")
  private String address;

  @Schema(description = "판매점 도로명 주소", example = "서울특별시 서초구 서초대로50길 82 정원빌딩")
  private String roadAddress;

  @Schema(description = "판매점 x좌표(경도)", example = "127.01275515884753")
  private String longitude;

  @Schema(description = "판매점 y좌표(위도)", example = "37.49206032952165")
  private String latitude;

  @Schema(description = "제로음료 판매 여부", example = "true")
  private Boolean status;

  @Schema(description = "제로음료 등록 이미지 목록")
  private Image[] images;

  @Schema(description = "판매점 상세페이지 URL", example = "http://place.map.kakao.com/25770215")
  private String placeUrl;

  public static Store of(com.zerozero.core.domain.entity.Store store) {
    if (store == null) {
      return null;
    }
    return Store.builder()
        .id(store.getId())
        .kakaoId(store.getKakaoId())
        .name(store.getName())
        .category(store.getCategory())
        .phone(store.getPhone())
        .address(store.getAddress())
        .roadAddress(store.getRoadAddress())
        .longitude(store.getLongitude())
        .latitude(store.getLatitude())
        .status(store.getStatus())
        .images(store.getImages())
        .placeUrl(store.getPlaceUrl())
        .build();
  }

  public static Store of(Document item) {
    if (item == null) {
      return null;
    }
    return Store.builder()
        .id(item.getStoreId())
        .kakaoId(item.getId())
        .name(item.getPlaceName())
        .category(item.getCategoryName())
        .phone(item.getPhone())
        .address(item.getAddressName())
        .roadAddress(item.getRoadAddressName())
        .longitude(item.getX())
        .latitude(item.getY())
        .status(item.isStatus())
        .images(item.getImages())
        .placeUrl(item.getPlaceUrl())
        .build();
  }
}
