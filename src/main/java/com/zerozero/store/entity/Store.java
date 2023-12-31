package com.zerozero.store.entity;

import com.zerozero.common.entity.BaseEntity;
import com.zerozero.naver.dto.SearchLocalResponse.SearchLocalItem;
import com.zerozero.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Store extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String category;

  private String address;

  private String roadAddress;

  private int mapx;

  private int mapy;

  private boolean selling;

  @ManyToOne
  private User user;

  public static Store of(User user, SearchLocalItem item, boolean isSelling) {
    return Store.builder()
        .name(item.getTitle())
        .category(item.getCategory())
        .address(item.getAddress())
        .roadAddress(item.getRoadAddress())
        .mapx(item.getMapx())
        .mapy(item.getMapy())
        .selling(isSelling)
        .user(user)
        .build();
  }
}
