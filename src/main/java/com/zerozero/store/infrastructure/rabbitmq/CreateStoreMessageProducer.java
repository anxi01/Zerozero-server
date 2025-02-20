package com.zerozero.store.infrastructure.rabbitmq;

import com.zerozero.core.infrastructure.rabbitmq.MessageProducer;

import java.util.UUID;

public class CreateStoreMessageProducer extends MessageProducer<CreateStoreQueueProperty, UUID> {

    private CreateStoreMessageProducer() {
        super(null, null);
    }

    public CreateStoreMessageProducer(CreateStoreQueueProperty queueProperty, UUID storeId) {
        super(queueProperty, storeId);
    }
}
