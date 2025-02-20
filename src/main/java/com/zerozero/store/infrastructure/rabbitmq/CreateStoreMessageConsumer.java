package com.zerozero.store.infrastructure.rabbitmq;

import com.zerozero.core.infrastructure.rabbitmq.MessageConsumer;
import com.zerozero.store.domain.service.CreateStoreMongoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CreateStoreMessageConsumer implements MessageConsumer<UUID> {

    private final CreateStoreMongoUseCase createStoreMongoUseCase;

    @Override
    @RabbitListener(queues = "${create-store-queue.queue}")
    public void consumeMessage(UUID storeId) {
        createStoreMongoUseCase.execute(storeId);
    }

}
