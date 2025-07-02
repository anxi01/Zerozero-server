package com.zerozero.core.infrastructure.rabbitmq

interface MessageConsumer<R> {
    fun consumeMessage(request: R)
}
