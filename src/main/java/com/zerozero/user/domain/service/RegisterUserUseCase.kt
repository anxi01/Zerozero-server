package com.zerozero.user.domain.service

import com.zerozero.core.util.JwtUtil
import com.zerozero.user.domain.repository.UserRepository
import com.zerozero.user.domain.request.RegisterRequest
import com.zerozero.user.domain.response.UserResponse
import com.zerozero.user.exception.UserErrorType
import com.zerozero.user.exception.UserException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RegisterUserUseCase(
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun execute(registerRequest: RegisterRequest, accessToken: String): UserResponse {
        val userId = jwtUtil.extractUserId(accessToken)
        val user = userRepository.findById(userId).orElseThrow { UserException(UserErrorType.NOT_EXIST_USER) }

        if (user.isRegistered()) {
            log.error("[RegisterUserUseCase] User with id {} is already registered", userId)
            throw UserException(UserErrorType.ALREADY_REGISTERED_USER)
        }

        user.completePendingUser(registerRequest.nickname)
        return UserResponse.from(user)
    }
}
