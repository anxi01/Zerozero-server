package com.zerozero.configuration.property.oauth;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("oauth.kakao")
public class KakaoOAuthProperty {

  protected String clientId;

  protected String clientSecret;

  protected String redirectUri;

  protected Set<String> scope;

  protected String tokenUri;

  protected String resourceUri;

  protected String authUri;
}
