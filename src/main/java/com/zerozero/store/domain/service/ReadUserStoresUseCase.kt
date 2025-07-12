package com.zerozero.store.domain.service

import com.zerozero.store.domain.repository.StoreRepository
import com.zerozero.store.domain.response.StoreResponse
import com.zerozero.user.domain.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReadUserStoresUseCase(
    private val storeRepository: StoreRepository
) {

    fun execute(user: User): List<StoreResponse> {
        return storeRepository.findAllByUserIdWithImages(user.id).map { StoreResponse.from(it) }
    }
}
