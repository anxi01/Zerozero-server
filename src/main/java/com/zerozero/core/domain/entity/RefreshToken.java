package com.zerozero.core.domain.entity;

import com.zerozero.core.domain.shared.BaseAutoIncrementEntity;
import jakarta.persistence.Entity;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@SuperBuilder
public class RefreshToken extends BaseAutoIncrementEntity {

  private String refreshToken;

  private UUID userId;

  public RefreshToken update(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }
}