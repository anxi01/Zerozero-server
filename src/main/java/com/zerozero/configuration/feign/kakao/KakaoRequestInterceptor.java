package com.zerozero.configuration.feign.kakao;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class KakaoRequestInterceptor implements RequestInterceptor {

    private static final String KAKAO_AUTHORIZATION_PREFIX = "KakaoAK ";

    @Value("${kakao.restApiKey}")
    private String restApiKey;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", KAKAO_AUTHORIZATION_PREFIX + restApiKey);
    }
}
