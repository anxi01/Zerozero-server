package com.zerozero.user.domain.repository

import com.zerozero.user.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {

    fun findByEmail(email: String): Optional<User>

    fun findAllByIdIn(userIds: List<UUID>): List<User>
}
