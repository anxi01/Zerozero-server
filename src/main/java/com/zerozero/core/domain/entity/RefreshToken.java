package com.zerozero.core.domain.entity;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "refreshToken", timeToLive = 604_800_000)
public class RefreshToken {

  @Id
  private UUID userId;

  private String refreshToken;
}
