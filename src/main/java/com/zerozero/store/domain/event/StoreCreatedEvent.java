package com.zerozero.store.domain.event;

import java.util.UUID;

public record StoreCreatedEvent(
        UUID storeId
) {
}
