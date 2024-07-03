package com.zerozero.user;

import com.zerozero.core.domain.vo.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

  private Image profileImage;
  private String nickname;
  private Long rank;
  private Long storeReportCount;
}