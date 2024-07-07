package com.zerozero.core.domain.vo;

import com.zerozero.core.domain.shared.Token;
import com.zerozero.core.domain.shared.ValueObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
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
@Schema(description = "리프레시 토큰")
public class RefreshToken extends ValueObject implements Token {

  private String token;

  public static RefreshToken of(String token) {
    if (token == null || token.isEmpty()) {
      return null;
    }
    return RefreshToken.builder()
        .token(token).build();
  }

  public static RefreshToken of(com.zerozero.core.domain.entity.RefreshToken entity) {
    if (entity == null) {
      return null;
    }
    return RefreshToken.builder()
        .token(entity.getRefreshToken())
        .build();
  }

  public com.zerozero.core.domain.entity.RefreshToken toEntity() {
    return com.zerozero.core.domain.entity.RefreshToken.builder()
        .refreshToken(this.getToken())
        .build();
  }
}
