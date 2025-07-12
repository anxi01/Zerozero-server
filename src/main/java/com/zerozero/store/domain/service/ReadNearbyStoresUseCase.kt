package com.zerozero.store.domain.service

import com.zerozero.auth.exception.AuthErrorType
import com.zerozero.auth.exception.AuthException
import com.zerozero.core.util.JwtUtil
import com.zerozero.store.domain.request.StoreLocationRequest
import com.zerozero.store.domain.response.StoreResponse
import com.zerozero.store.infrastructure.mongodb.StoreMongoRepository
import com.zerozero.user.domain.repository.UserRepository
import com.zerozero.user.exception.UserErrorType
import com.zerozero.user.exception.UserException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReadNearbyStoresUseCase(
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository,
    private val storeMongoRepository: StoreMongoRepository
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun execute(storeLocationRequest: StoreLocationRequest): List<StoreResponse> {
        val accessToken = storeLocationRequest.accessToken
        if (jwtUtil.isTokenExpired(accessToken)) {
            log.error("[ReadNearbyStoresUseCase] Expired access token")
            throw AuthException(AuthErrorType.EXPIRED_TOKEN)
        }
        val userId = jwtUtil.extractUserId(accessToken)
        val user = userRepository.findById(userId).orElseThrow { UserException(UserErrorType.NOT_EXIST_USER) }
        val mongoStores = storeMongoRepository.findStoresWithinCoordinatesRadius(
            storeLocationRequest.longitude,
            storeLocationRequest.latitude,
            DEFAULT_RADIUS
        )
        return mongoStores.map { StoreResponse.from(it) }
    }

    companion object {
        const val DEFAULT_RADIUS: Double = 1000.0
    }
}
