package com.zerozero.user.application

import com.zerozero.store.domain.service.GetStoreUserRankUseCase
import com.zerozero.user.domain.model.User
import com.zerozero.user.presentation.response.ReadUserInfoResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val getStoreUserRankUseCase: GetStoreUserRankUseCase
) {

    fun readUserInfo(user: User): ReadUserInfoResponse {
        val storeUserRankProjection = getStoreUserRankUseCase.execute(user.id)
        return ReadUserInfoResponse.of(user, storeUserRankProjection)
    }
}
