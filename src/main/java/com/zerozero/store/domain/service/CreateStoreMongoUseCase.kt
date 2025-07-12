package com.zerozero.store.domain.service

import com.zerozero.store.domain.repository.StoreRepository
import com.zerozero.store.exception.StoreErrorType
import com.zerozero.store.exception.StoreException
import com.zerozero.store.infrastructure.mongodb.Store
import com.zerozero.store.infrastructure.mongodb.StoreMongoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CreateStoreMongoUseCase(
    private val storeRepository: StoreRepository,
    private val storeMongoRepository: StoreMongoRepository
) {

    fun execute(storeId: UUID) {
        val store = storeRepository.findById(storeId).orElseThrow { StoreException(StoreErrorType.NOT_EXIST_STORE) }
        val storeMongo = Store.from(store)
        storeMongoRepository.save(storeMongo)
    }
}
