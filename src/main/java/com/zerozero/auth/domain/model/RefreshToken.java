package com.zerozero.auth.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@Builder
@RedisHash(value = "refreshToken", timeToLive = 604_800_000)
public class RefreshToken {

    @Id
    private UUID userId;

    private String refreshToken;
}
