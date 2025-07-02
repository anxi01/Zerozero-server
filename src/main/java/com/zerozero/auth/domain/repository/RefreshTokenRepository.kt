package com.zerozero.auth.domain.repository

import com.zerozero.auth.domain.model.RefreshToken
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RefreshTokenRepository : CrudRepository<RefreshToken, UUID>
