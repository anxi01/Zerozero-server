package com.zerozero.core.infrastructure.rabbitmq;

public interface MessageConsumer<R> {

    void consumeMessage(R request);
}
