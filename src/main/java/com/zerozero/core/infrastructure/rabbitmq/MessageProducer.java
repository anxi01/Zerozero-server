package com.zerozero.core.infrastructure.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Log4j2
@RequiredArgsConstructor
public abstract class MessageProducer<T extends BaseQueueProperty, R> {

    private final RabbitTemplate rabbitTemplate;

    protected final T queueProperty;

    public void publishMessage(R request) {
        try {
            rabbitTemplate.convertAndSend(queueProperty.getExchange(), queueProperty.getRoutingKey(), request);
        } catch (Exception e) {
            log.error("[MessageProducer] - publishMessage Failed", e);
        }
    }
}
