package com.zerozero.store.infrastructure.kakao.search.application;

import com.zerozero.configuration.feign.kakao.KakaoFeignConfiguration;
import com.zerozero.store.infrastructure.kakao.search.request.KakaoSearchRequest;
import com.zerozero.store.infrastructure.kakao.search.response.KakaoSearchResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "kakaoSearchClient", url = "${kakao.url}", configuration = KakaoFeignConfiguration.class)
public interface KakaoSearchClient {

    @GetMapping("/search/keyword.json")
    KakaoSearchResponseWrapper search(@SpringQueryMap KakaoSearchRequest request);
}
