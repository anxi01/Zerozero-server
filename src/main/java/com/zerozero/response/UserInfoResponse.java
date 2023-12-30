package com.zerozero.response;

import com.zerozero.domain.Store;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

  private String nickname;

  private List<Store> stores;
}
