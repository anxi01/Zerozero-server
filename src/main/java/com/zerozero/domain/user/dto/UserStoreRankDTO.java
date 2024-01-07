package com.zerozero.domain.user.dto;

import lombok.Getter;

@Getter
public class UserStoreRankDTO {

  private String nickname;
  private Long storeCount;

  public UserStoreRankDTO(String nickname, long storeCount) {
    this.nickname = nickname;
    this.storeCount = storeCount;
  }
}
