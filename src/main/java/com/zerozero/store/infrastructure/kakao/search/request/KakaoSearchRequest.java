package com.zerozero.store.infrastructure.kakao.search.request;

import com.zerozero.store.infrastructure.kakao.search.core.CategoryGroupCode;
import lombok.Builder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Builder
public record KakaoSearchRequest(
        String query,
        CategoryGroupCode categoryGroupCode,
        String longitude,
        String latitude,
        Integer radius,
        String rect,
        Integer page,
        Integer size,
        String sort
) {

    public MultiValueMap<String, String> createQueryParams() {
        LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        if (query != null && !query.isEmpty()) {
            queryParams.add("query", query);
        }
        if (categoryGroupCode != null) {
            queryParams.add("category_group_code", categoryGroupCode.name());
        }
        if (longitude != null && !longitude.isEmpty()) {
            queryParams.add("x", longitude);
        }
        if (latitude != null && !latitude.isEmpty()) {
            queryParams.add("y", latitude);
        }
        if (radius != null) {
            queryParams.add("radius", String.valueOf(radius));
        }
        if (rect != null && !rect.isEmpty()) {
            queryParams.add("rect", rect);
        }
        if (page != null) {
            queryParams.add("page", String.valueOf(page));
        }
        if (size != null) {
            queryParams.add("size", String.valueOf(size));
        }
        if (sort != null && !sort.isEmpty()) {
            queryParams.add("sort", sort);
        }

        return queryParams;
    }
}

