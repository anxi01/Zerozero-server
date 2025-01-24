package com.zerozero.core.domain.entity;

import com.zerozero.core.domain.shared.BaseEntity;
import com.zerozero.core.domain.vo.ZeroDrink;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "reviewId")
  private List<ReviewLike> reviewLikes = new ArrayList<>();

  public static Review of(String content, ZeroDrink[] zeroDrinks, User user, Store store) {
    return Review.builder()
        .content(content)
        .zeroDrinks(zeroDrinks)
        .storeId(store.getId())
        .userId(user.getId())
        .build();
  }

  public boolean hasUserReviewed(User user) {
    return this.userId.equals(user.getId());
  }

  public void deleted(boolean deleted) {
    setDeleted(deleted);
  }

  public enum Filter {
    RECENT, RECOMMEND
  }
}
