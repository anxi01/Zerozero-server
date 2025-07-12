package com.zerozero.store.domain.service

import com.zerozero.store.domain.event.StoreCreatedEvent
import com.zerozero.store.domain.model.Store.Companion.create
import com.zerozero.store.domain.repository.StoreRepository
import com.zerozero.store.presentation.request.CreateStoreRequest
import com.zerozero.user.domain.model.User
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CreateStoreUseCase(
    private val storeRepository: StoreRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun execute(createStoreRequest: CreateStoreRequest, user: User): UUID {
        val store = create(user.id, createStoreRequest)
        storeRepository.save(store)

        val storeId = store.id
        eventPublisher.publishEvent(StoreCreatedEvent(storeId))
        return storeId
    }
}
