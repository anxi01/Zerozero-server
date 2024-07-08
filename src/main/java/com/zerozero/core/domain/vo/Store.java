package com.zerozero.core.domain.vo;

import com.zerozero.core.domain.shared.ValueObject;
import com.zerozero.external.naver.search.dto.SearchLocalResponse.SearchLocalItem;
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

  @Schema(description = "판매점 이름", example = "제로 피자")
  private String name;

  @Schema(description = "판매 종류", example = "양식>피자")
  private String category;

  @Schema(description = "판매점 주소", example = "서울특별시 서초구 서초동")
  private String address;

  @Schema(description = "판매점 도로명 주소", example = "서울특별시 서초구 서초대로50길 82 정원빌딩")
  private String roadAddress;

  @Schema(description = "판매점 x좌표", example = "1270136455")
  private Integer mapx;

  @Schema(description = "판매점 y좌표", example = "374895501")
  private Integer mapy;

  @Schema(description = "제로음료 판매 여부", example = "true")
  private Boolean status;

  @Schema(description = "제로음료 등록 이미지 목록")
  private Image[] images;

  public static Store of(com.zerozero.core.domain.entity.Store store) {
    if (store == null) {
      return null;
    }
    return Store.builder()
        .id(store.getId())
        .name(store.getName())
        .category(store.getCategory())
        .address(store.getAddress())
        .roadAddress(store.getRoadAddress())
        .mapx(store.getMapx())
        .mapy(store.getMapy())
        .status(store.getStatus())
        .images(store.getImages())
        .build();
  }

  public static Store of(SearchLocalItem item) {
    if (item == null) {
      return null;
    }
    return Store.builder()
        .id(item.getStoreId())
        .name(item.getTitle())
        .category(item.getCategory())
        .address(item.getAddress())
        .roadAddress(item.getRoadAddress())
        .mapx(item.getMapx())
        .mapy(item.getMapy())
        .status(item.isStatus())
        .images(null)
        .build();
  }
}
