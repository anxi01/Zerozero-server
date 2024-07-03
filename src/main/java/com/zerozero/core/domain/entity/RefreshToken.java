package com.zerozero.core.domain.entity;

import com.zerozero.core.domain.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class RefreshToken extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String refreshToken;

  private UUID userId;

  public RefreshToken(String refreshToken, User user) {
    this.refreshToken = refreshToken;
    this.userId = user.getId();
  }

  public RefreshToken update(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }
}