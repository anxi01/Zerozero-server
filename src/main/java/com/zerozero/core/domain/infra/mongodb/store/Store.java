package com.zerozero.core.domain.infra.mongodb.store;

import com.zerozero.core.domain.infra.mongodb.GeoJsonConverter;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "store")
public class Store {

  private UUID storeId;

  private String kakaoId;

  private String name;

  private String category;

  private String phone;

  private String address;

  private String roadAddress;

  private String longitude;

  private String latitude;

  @Builder.Default
  private Boolean status = true;

  private GeoJsonPoint location;

  private String placeUrl;

  private UUID userId;

  public static Store of(com.zerozero.core.domain.entity.Store store) {
    if (store == null) {
      return null;
    }
    return Store.builder()
        .storeId(store.getId())
        .kakaoId(store.getKakaoId())
        .name(store.getName())
        .category(store.getCategory())
        .phone(store.getPhone())
        .address(store.getAddress())
        .roadAddress(store.getRoadAddress())
        .longitude(store.getLongitude())
        .latitude(store.getLatitude())
        .location(GeoJsonConverter.of(store.getLongitude(), store.getLatitude()))
        .placeUrl(store.getPlaceUrl())
        .userId(store.getUserId())
        .build();
  }
}
