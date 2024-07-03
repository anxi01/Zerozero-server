package com.zerozero.core.domain.entity;

import com.zerozero.core.domain.shared.BaseEntity;
import com.zerozero.core.domain.vo.ZeroDrink;
import com.zerozero.review.ReviewRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.UUID;
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

  public static Review of(ReviewRequest request, User user, Store store) {
    return Review.builder()
        .zeroDrinks(request.getZeroDrinks())
        .content(request.getContent())
        .storeId(store.getId())
        .userId(user.getId())
        .build();
  }

  public void editReview(ReviewRequest request) {
    this.zeroDrinks = request.getZeroDrinks();
    this.content = request.getContent();
  }
}
