package com.zerozero.domain.store.domain;

import com.zerozero.domain.store.dto.request.ReviewRequest;
import com.zerozero.domain.user.domain.User;
import com.zerozero.global.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public static Review of(ReviewRequest request, User user, Store store) {
    return Review.builder()
        .content(request.getContent())
        .store(store)
        .user(user)
        .build();
  }

  public void editReview(ReviewRequest request) {
    this.content = request.getContent();
  }
}
