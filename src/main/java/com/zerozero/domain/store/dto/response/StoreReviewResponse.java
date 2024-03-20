package com.zerozero.domain.store.dto.response;

import com.zerozero.domain.store.domain.Store;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class StoreReviewResponse {


  @Builder
  @Getter
  public static class StoreInfoResponse {

    private Long id;

    private String name;

    private String category;

    private String address;

    private String roadAddress;

    private int mapx;

    private int mapy;

    private boolean selling;

    private List<String> images;

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
          .images(store.getImageUrl())
          .build();
    }
  }
}
