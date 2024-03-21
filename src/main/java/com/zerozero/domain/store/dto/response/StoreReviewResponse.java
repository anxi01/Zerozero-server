package com.zerozero.domain.store.dto.response;

import com.zerozero.domain.store.domain.Review;
import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.store.domain.ZeroDrinks;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
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

  public static StoreReviewResponse of(Store store, List<Review> reviews, List<Integer> likeCounts) {
    return StoreReviewResponse.builder()
        .storeInfo(StoreInfoResponse.from(store))
        .reviews(IntStream.range(0, reviews.size())
            .mapToObj(i -> ReviewResponse.of(reviews.get(i), likeCounts.get(i)))
            .toList()).build();
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
    private List<ZeroDrinks> zeroDrinks;
    private String content;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResponse of(Review review, int likeCount) {
      return ReviewResponse.builder()
          .reviewId(review.getId())
          .userId(review.getUser().getId())
          .nickname(review.getUser().getNickname())
          .zeroDrinks(review.getZeroDrinks())
          .content(review.getContent())
          .likeCount(likeCount)
          .createdAt(review.getCreatedAt())
          .updatedAt(review.getUpdatedAt())
          .build();
    }
  }
}
