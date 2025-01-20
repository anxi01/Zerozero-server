package com.zerozero.core.domain.entity;

import com.zerozero.core.domain.shared.BaseEntity;
import com.zerozero.core.domain.vo.Image;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class User extends BaseEntity {

  private String nickname;

  private String email;

  private Image profileImage;

  @Enumerated(EnumType.STRING)
  private Status status;

  public void completePendingUser(String nickname) {
    this.nickname = nickname;
    this.status = Status.COMPLETED;
  }

  public void updateNickname(String nickname) {
    if (nickname == null) {
      return;
    }
    this.nickname = nickname;
  }

  public void uploadProfileImage(Image image) {
    this.profileImage = image;
  }
}
