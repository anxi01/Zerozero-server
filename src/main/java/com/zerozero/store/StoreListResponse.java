package com.zerozero.store;

import com.zerozero.external.naver.SearchLocalResponse.SearchLocalItem;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreListResponse {

  private List<SearchLocalItem> items;
}
