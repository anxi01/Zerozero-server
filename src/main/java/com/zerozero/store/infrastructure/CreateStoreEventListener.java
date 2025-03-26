package com.zerozero.store.infrastructure;

import com.zerozero.store.domain.event.StoreCreatedEvent;
import com.zerozero.store.infrastructure.rabbitmq.CreateStoreMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateStoreEventListener {

    private final CreateStoreMessageProducer createStoreMessageProducer;

    @TransactionalEventListener(
            value = StoreCreatedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleCreateStoreEvent(StoreCreatedEvent storeCreatedEvent) {
        try {
            createStoreMessageProducer.publishMessage(storeCreatedEvent.storeId());
        } catch (Exception e) {
            log.error("[CreateStoreEventListener] Failed to publish CreateStoreEvent to RabbitMQ", e);
        }
    }
}
