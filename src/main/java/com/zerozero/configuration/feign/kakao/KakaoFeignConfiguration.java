package com.zerozero.configuration.feign.kakao;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoFeignConfiguration {

    @Bean
    public RequestInterceptor kakaoRequestInterceptor() {
        return new KakaoRequestInterceptor();
    }

    @Bean
    public ErrorDecoder kakaoFeignErrorDecoder() {
        return new KakaoFeignErrorDecoder();
    }
}
