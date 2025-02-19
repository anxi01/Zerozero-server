package com.zerozero.user.domain.model;

import com.zerozero.core.domain.BaseEntity;
import com.zerozero.image.domain.model.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class User extends BaseEntity {

    private String nickname;

    private String email;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Image profileImage;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    public void completePendingUser(String nickname) {
        this.nickname = nickname;
        this.userStatus = UserStatus.COMPLETED;
    }

    public void updateNickname(String nickname) {
        if (this.nickname.equals(nickname)) {
            return;
        }
        this.nickname = nickname;
    }

    public boolean isRegistered() {
        return this.getUserStatus() == UserStatus.COMPLETED;
    }

    public void uploadProfileImage(String profileImage) {
        this.profileImage = Image.from(profileImage);
    }

}
