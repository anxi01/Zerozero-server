package com.zerozero.core.domain.entity;

import com.zerozero.core.domain.shared.BaseEntity;
import com.zerozero.core.domain.vo.Image;
import com.zerozero.external.kakao.search.dto.KeywordSearchResponse.Document;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@SuperBuilder
public class Store extends BaseEntity {

  private String kakaoId;

  private String name;

  private String category;

  private String phone;

  private String address;

  private String roadAddress;

  private String longitude;

  private String latitude;

  @Builder.Default
  private Boolean status = false;

  @Column(columnDefinition = "JSON")
  private Image[] images;

  private String placeUrl;

  private UUID userId;

  public static Store of(UUID userId, Document store, List<String> uploadImages) {
    return Store.builder()
        .kakaoId(store.getId())
        .name(store.getPlaceName())
        .category(store.getCategoryName())
        .phone(store.getPhone())
        .address(store.getAddressName())
        .roadAddress(store.getRoadAddressName())
        .longitude(store.getX())
        .latitude(store.getY())
        .status(true)
        .images(uploadImages.stream().map(Image::convertUrlToImage).toArray(Image[]::new))
        .placeUrl(store.getPlaceUrl())
        .userId(userId)
        .build();
  }
}
