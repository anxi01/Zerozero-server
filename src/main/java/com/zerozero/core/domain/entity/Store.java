package com.zerozero.core.domain.entity;

import com.zerozero.core.domain.shared.BaseEntity;
import com.zerozero.core.domain.vo.Image;
import com.zerozero.external.naver.search.dto.SearchLocalResponse.SearchLocalItem;
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

  private String name;

  private String category;

  private String address;

  private String roadAddress;

  private Integer mapx;

  private Integer mapy;

  @Builder.Default
  private Boolean status = false;

  @Column(columnDefinition = "JSON")
  private Image[] images;

  private UUID userId;

  public static Store of(UUID userId, SearchLocalItem item, List<String> uploadImages) {
    return Store.builder()
        .name(item.getTitle())
        .category(item.getCategory())
        .address(item.getAddress())
        .roadAddress(item.getRoadAddress())
        .mapx(item.getMapx())
        .mapy(item.getMapy())
        .status(true)
        .images(uploadImages.stream().map(Image::convertUrlToImage).toArray(Image[]::new))
        .userId(userId)
        .build();
  }
}
