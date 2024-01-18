package com.zerozero.domain.store.domain;

import com.zerozero.domain.naver.dto.response.SearchLocalResponse.SearchLocalItem;
import com.zerozero.domain.user.domain.User;
import com.zerozero.global.common.domain.BaseEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

  private boolean selling = false;

  @ManyToOne
  private User user;

  @ElementCollection
  private List<String> imageUrl;

  public static Store of(User user, SearchLocalItem item, List<String> uploadImages) {
    return Store.builder()
        .name(item.getTitle())
        .category(item.getCategory())
        .address(item.getAddress())
        .roadAddress(item.getRoadAddress())
        .mapx(item.getMapx())
        .mapy(item.getMapy())
        .selling(true)
        .user(user)
        .imageUrl(uploadImages)
        .build();
  }
}
