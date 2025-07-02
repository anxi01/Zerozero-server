package com.zerozero.core.infrastructure.rabbitmq

open class BaseQueueProperty(
    var exchange: String? = null,
    var queue: String? = null,
    var routingKey: String? = null
)
