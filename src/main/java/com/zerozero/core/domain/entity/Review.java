package com.zerozero.core.domain.entity;

import com.zerozero.core.domain.shared.BaseEntity;
import com.zerozero.core.domain.vo.ZeroDrink;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
public class Review extends BaseEntity {

  @Column(columnDefinition = "JSON")
  private ZeroDrink[] zeroDrinks;

  private String content;

  private UUID userId;

  private UUID storeId;

  public static Review of(String content, ZeroDrink[] zeroDrinks, User user, Store store) {
    return Review.builder()
        .content(content)
        .zeroDrinks(zeroDrinks)
        .storeId(store.getId())
        .userId(user.getId())
        .build();
  }

  public static List<Review> filter(List<Review> reviews, Filter filter, List<Integer> reviewCount) {
    if (filter == null || reviews == null || reviews.isEmpty()) {
      return reviews;
    }
    switch (filter) {
      case RECENT -> {
        return reviews.stream().sorted(Comparator.comparing(Review::getCreatedAt).reversed()).collect(
            Collectors.toList());
      }
      case RECOMMEND -> {
        if (reviewCount == null || reviewCount.isEmpty() || reviewCount.size() != reviews.size()) {
          return reviews;
        }
        return IntStream.range(0, reviews.size())
            .boxed()
            .sorted((i, j) -> reviewCount.get(j).compareTo(reviewCount.get(i)))
            .map(reviews::get)
            .collect(Collectors.toList());
      }
      default -> {
        return reviews;
      }
    }
  }

  public boolean hasUserReviewed(User user) {
    return this.userId.equals(user.getId());
  }

  public enum Filter {
    RECENT, RECOMMEND
  }
}
