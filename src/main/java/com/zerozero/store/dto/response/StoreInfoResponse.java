package com.zerozero.store.dto.response;

import com.zerozero.store.entity.Store;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreInfoResponse {

  private Long id;

  private String name;

  private String category;

  private String address;

  private String roadAddress;

  private int mapx;

  private int mapy;

  private boolean selling;

  public static StoreInfoResponse from(Store store) {
    return StoreInfoResponse.builder()
        .id(store.getId())
        .name(store.getName())
        .category(store.getCategory())
        .address(store.getAddress())
        .roadAddress(store.getRoadAddress())
        .mapx(store.getMapx())
        .mapy(store.getMapy())
        .selling(store.isSelling())
        .build();
  }
}
