package com.zerozero.auth.application

import com.zerozero.auth.domain.model.RefreshToken
import com.zerozero.auth.domain.repository.RefreshTokenRepository
import com.zerozero.auth.exception.AuthErrorType
import com.zerozero.auth.exception.AuthException
import com.zerozero.auth.infrastructure.oauth.core.Provider
import com.zerozero.auth.presentation.response.LoginResponse
import com.zerozero.auth.presentation.response.TokenResponse
import com.zerozero.core.util.JwtUtil
import com.zerozero.user.domain.model.User
import com.zerozero.user.domain.repository.UserRepository
import com.zerozero.user.domain.response.UserResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class HandleOAuthLoginService(
    private val oAuthRestClientFactory: OAuthRestClientFactory,
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun execute(code: String, providerName: String): LoginResponse {
        val provider = Provider.valueOf(providerName.uppercase())
        val oAuthRestClient = oAuthRestClientFactory.getOAuthRestClient(provider)

        val oAuthAccessTokenResponse = oAuthRestClient.getAccessToken(code)
            ?: throw AuthException(AuthErrorType.ACCESS_TOKEN_NOT_ISSUED)
                .also { log.error("[OAuthLogin] Failed to get access token for provider: {}, code: {}", provider, code) }

        val oAuthResourceResponse = oAuthRestClient.getResource(oAuthAccessTokenResponse.accessToken)
            ?: throw AuthException(AuthErrorType.NOT_EXIST_RESOURCE_RESPONSE)
                .also { log.error("[OAuthLogin] Failed to get resource from provider: {}", provider) }

        val userEmail = oAuthResourceResponse.email
        val savedUser = userRepository.findByEmail(userEmail) ?: userRepository.save(User.createPendingUser(userEmail))
        return generateAndBuildResponse(savedUser)
    }

    private fun generateAndBuildResponse(user: User): LoginResponse {
        val accessToken = jwtUtil.generateAccessToken(user)
        val refreshToken = jwtUtil.generateRefreshToken(user)
        refreshTokenRepository.save(RefreshToken(user.id, refreshToken))
        return LoginResponse.of(UserResponse.from(user), TokenResponse.of(accessToken, refreshToken))
    }
}
