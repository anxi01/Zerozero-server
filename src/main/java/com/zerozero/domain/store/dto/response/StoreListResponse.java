package com.zerozero.domain.store.dto.response;

import com.zerozero.domain.naver.dto.response.SearchLocalResponse.SearchLocalItem;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreListResponse {

  private List<SearchLocalItem> items;
}
