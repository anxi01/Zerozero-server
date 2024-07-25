package com.zerozero.external.kakao.search.core.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("kakao")
@Getter
@Setter
public class KakaoProperty {

  private String restApiKey;

  private String keywordUrl;
}
