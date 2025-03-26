package com.zerozero.store.infrastructure.rabbitmq;

import com.zerozero.core.infrastructure.rabbitmq.MessageProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateStoreMessageProducer extends MessageProducer<CreateStoreQueueProperty, UUID> {

    public CreateStoreMessageProducer(RabbitTemplate rabbitTemplate, CreateStoreQueueProperty queueProperty) {
        super(rabbitTemplate, queueProperty);
    }
}
