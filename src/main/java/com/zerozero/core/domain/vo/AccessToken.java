package com.zerozero.core.domain.vo;

import com.zerozero.core.domain.shared.Token;
import com.zerozero.core.domain.shared.ValueObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "액세스 토큰")
public class AccessToken extends ValueObject implements Token {

  private String token;

  public static AccessToken of(String token) {
    if (token == null || token.isEmpty()) {
      return null;
    }
    return new AccessToken(token);
  }
}
