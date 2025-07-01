package com.zerozero.auth.domain.model;

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.util.*

@RedisHash(value = "refreshToken", timeToLive = 604800)
class RefreshToken(
    @Id
    val userId: UUID,
    val refreshToken: String
)
