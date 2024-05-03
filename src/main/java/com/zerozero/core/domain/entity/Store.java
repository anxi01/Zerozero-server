package com.zerozero.core.domain.entity;

import com.zerozero.core.domain.shared.BaseEntity;
import com.zerozero.external.naver.SearchLocalResponse.SearchLocalItem;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
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
