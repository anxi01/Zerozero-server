package com.zerozero.store;

import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.Store;
import com.zerozero.core.domain.vo.Image;
import com.zerozero.core.domain.vo.ZeroDrink;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
  private List<ZeroDrink> top3ZeroDrinks;
  private List<ReviewResponse> reviews;

  public static StoreReviewResponse of(Store store, List<Review> reviews, List<Integer> likeCounts,
      List<ZeroDrink> top3ZeroDrinks) {
    return StoreReviewResponse.builder()
        .storeInfo(StoreInfoResponse.from(store))
        .top3ZeroDrinks(top3ZeroDrinks)
        .reviews(IntStream.range(0, reviews.size())
            .mapToObj(i -> ReviewResponse.of(reviews.get(i), likeCounts.get(i)))
            .toList()).build();
  }

  @Builder
  @Getter
  public static class StoreInfoResponse {

    private UUID storeId;
    private String name;
    private String category;
    private String address;
    private String roadAddress;
    private int mapx;
    private int mapy;
    private boolean selling;
    private List<Image> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StoreInfoResponse from(Store store) {
      return StoreInfoResponse.builder()
          .storeId(store.getId())
          .name(store.getName())
          .category(store.getCategory())
          .address(store.getAddress())
          .roadAddress(store.getRoadAddress())
          .mapx(store.getMapx())
          .mapy(store.getMapy())
          .selling(store.getStatus())
          .images(Arrays.stream(store.getImages()).collect(Collectors.toList()))
          .createdAt(store.getCreatedAt())
          .updatedAt(store.getUpdatedAt())
          .build();
    }
  }

  @Getter
  @Builder
  public static class ReviewResponse {

    private UUID reviewId;
    private UUID userId;
    private String nickname;
    private List<ZeroDrink> zeroDrinks;
    private String content;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResponse of(Review review, int likeCount) {
      return ReviewResponse.builder()
          .reviewId(review.getId())
          .userId(review.getUserId())
          .nickname(review.getUser().getNickname())
          .zeroDrinks(Arrays.stream(review.getZeroDrinks()).collect(Collectors.toList()))
          .content(review.getContent())
          .likeCount(likeCount)
          .createdAt(review.getCreatedAt())
          .updatedAt(review.getUpdatedAt())
          .build();
    }
  }
}
