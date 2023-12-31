package com.zerozero.store.dto.response;

import com.zerozero.naver.dto.SearchLocalResponse.SearchLocalItem;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreListResponse {

  private List<SearchLocalItem> items;
}
