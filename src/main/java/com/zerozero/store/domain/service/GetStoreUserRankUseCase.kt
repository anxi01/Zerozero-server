package com.zerozero.store.domain.service

import com.zerozero.store.domain.repository.StoreRepository
import com.zerozero.store.domain.response.StoreUserRankProjection
import com.zerozero.store.exception.StoreErrorType
import com.zerozero.store.exception.StoreException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class GetStoreUserRankUseCase(
    private val storeRepository: StoreRepository
) {

    fun execute(userId: UUID): StoreUserRankProjection {
        return storeRepository.findStoreUserRank(userId)
            .orElseThrow { StoreException(StoreErrorType.USER_RANK_NOT_AVAILABLE) }
    }
}
