package com.zerozero.auth.application

import com.zerozero.auth.domain.repository.RefreshTokenRepository
import com.zerozero.auth.exception.AuthErrorType
import com.zerozero.auth.exception.AuthException
import com.zerozero.auth.presentation.response.TokenResponse
import com.zerozero.core.util.JwtUtil
import com.zerozero.user.domain.repository.UserRepository
import com.zerozero.user.exception.UserErrorType
import com.zerozero.user.exception.UserException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RefreshUserTokenService(
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun execute(refreshToken: String): TokenResponse {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            log.error("[RefreshUserTokenService] Expired refresh token")
            throw AuthException(AuthErrorType.EXPIRED_TOKEN)
        }

        val userId = jwtUtil.extractUserId(refreshToken)

        val user = userRepository.findById(userId)
            .orElseThrow { UserException(UserErrorType.NOT_EXIST_USER) }

        val existingRefreshToken = refreshTokenRepository.findById(user.id)
            .orElseThrow { AuthException(AuthErrorType.NOT_EXIST_REFRESH_TOKEN) }

        if (existingRefreshToken.refreshToken == refreshToken &&
            jwtUtil.isTokenValid(refreshToken, user)) {

            val accessToken = jwtUtil.generateAccessToken(user)
            return TokenResponse.of(accessToken, refreshToken)
        }

        throw AuthException(AuthErrorType.TOKEN_REFRESH_FAILED)
    }
}
