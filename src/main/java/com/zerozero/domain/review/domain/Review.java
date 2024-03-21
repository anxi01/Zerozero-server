package com.zerozero.domain.review.domain;

import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.review.dto.request.ReviewRequest;
import com.zerozero.domain.user.domain.User;
import com.zerozero.global.common.domain.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "review_zero_drinks", joinColumns = @JoinColumn(name = "review_id"))
  private List<ZeroDrinks> zeroDrinks = new ArrayList<>();

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public static Review of(ReviewRequest request, User user, Store store) {
    return Review.builder()
        .zeroDrinks(request.getZeroDrinks())
        .content(request.getContent())
        .store(store)
        .user(user)
        .build();
  }

  public void editReview(ReviewRequest request) {
    this.zeroDrinks = request.getZeroDrinks();
    this.content = request.getContent();
  }
}
