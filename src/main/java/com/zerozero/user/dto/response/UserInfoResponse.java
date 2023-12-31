package com.zerozero.user.dto.response;

import com.zerozero.store.dto.response.StoreInfoResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

  private String nickname;

  private List<StoreInfoResponse> stores;
}
