package com.zerozero.domain.store.dto.response;

import com.zerozero.domain.store.domain.Review;
import com.zerozero.domain.store.domain.Store;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreReviewResponse {

  private StoreInfoResponse storeInfo;
  private List<ReviewResponse> reviews;

  public static StoreReviewResponse of(Store store, List<Review> reviews) {
    return StoreReviewResponse.builder()
        .storeInfo(StoreInfoResponse.from(store))
        .reviews(reviews.stream().map(ReviewResponse::from).toList())
        .build();
  }

  @Builder
  @Getter
  public static class StoreInfoResponse {

    private Long storeId;

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
          .storeId(store.getId())
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

  @Getter
  @Builder
  public static class ReviewResponse {

    private Long reviewId;
    private Long userId;
    private String nickname;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResponse from(Review review) {
      return ReviewResponse.builder()
          .reviewId(review.getId())
          .userId(review.getUser().getId())
          .nickname(review.getUser().getNickname())
          .rating(review.getRating())
          .content(review.getContent())
          .createdAt(review.getCreatedAt())
          .updatedAt(review.getUpdatedAt())
          .build();
    }
  }
}
