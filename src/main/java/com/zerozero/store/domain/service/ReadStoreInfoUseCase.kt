package com.zerozero.store.domain.service

import com.zerozero.store.domain.repository.StoreRepository
import com.zerozero.store.domain.response.StoreResponse
import com.zerozero.store.exception.StoreErrorType
import com.zerozero.store.exception.StoreException
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class ReadStoreInfoUseCase(
    private val storeRepository: StoreRepository
) {

    @Cacheable(value = ["stores"], key = "#storeId")
    fun execute(storeId: UUID): StoreResponse {
        val store = storeRepository.findByIdWithImages(storeId) ?: throw StoreException(StoreErrorType.NOT_EXIST_STORE)
        return StoreResponse.from(store)
    }
}
