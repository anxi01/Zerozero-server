package com.zerozero.core.domain.entity;

import com.zerozero.core.domain.shared.BaseAutoIncrementEntity;
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
public class ReviewLike extends BaseAutoIncrementEntity {

  private UUID reviewId;

  private UUID userId;

  public ReviewLike(Review review, User user) {
    this.reviewId = review.getId();
    this.userId = user.getId();
  }
}