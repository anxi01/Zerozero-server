package com.zerozero.store.infrastructure;

import com.zerozero.store.domain.event.CreateStoreEvent;
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
            value = CreateStoreEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleCreateStoreEvent(CreateStoreEvent createStoreEvent) {
        try {
            createStoreMessageProducer.publishMessage(createStoreEvent.storeId());
        } catch (Exception e) {
            log.error("[CreateStoreEventListener] Failed to publish CreateStoreEvent to RabbitMQ", e);
        }
    }
}
