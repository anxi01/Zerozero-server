package com.zerozero.store.infrastructure.kakao.search.request;

import com.zerozero.store.infrastructure.kakao.search.core.CategoryGroupCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoSearchRequest {
    private String query;
    private CategoryGroupCode categoryGroupCode;
    private String x;
    private String y;
    private Integer radius;
    private String rect;
    private Integer page;
    private Integer size;
    private String sort;
}
