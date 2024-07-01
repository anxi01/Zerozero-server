package com.zerozero.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

  private String profileImage;
  private String nickname;
  private Long rank;
  private Long storeReportCount;
}