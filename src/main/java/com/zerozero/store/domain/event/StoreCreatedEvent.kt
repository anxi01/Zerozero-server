package com.zerozero.store.domain.event

import java.util.*

@JvmRecord
data class StoreCreatedEvent(
    val storeId: UUID
)
