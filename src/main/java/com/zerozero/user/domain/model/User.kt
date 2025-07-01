package com.zerozero.user.domain.model

import com.zerozero.core.domain.BaseEntity
import com.zerozero.image.domain.model.Image
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
class User(

    @Column(name = "nickname", nullable = true)
    var nickname: String? = null,

    @Column(name = "email", nullable = false, unique = true)
    var email: String,

    @Embedded
    var profileImage: Image? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    var userStatus: UserStatus
) : BaseEntity() {

    fun completePendingUser(nickname: String) {
        this.nickname = nickname
        this.userStatus = UserStatus.COMPLETED
    }

    fun updateNickname(nickname: String) {
        if (this.nickname == nickname) return
        this.nickname = nickname
    }

    fun isRegistered(): Boolean = this.userStatus == UserStatus.COMPLETED

    fun uploadProfileImage(profileImage: String) {
        this.profileImage = Image(profileImage)
    }

    companion object {
        @JvmStatic
        fun createPendingUser(email: String): User {
            return User(
                email = email,
                userStatus = UserStatus.PENDING
            )
        }
    }
}
