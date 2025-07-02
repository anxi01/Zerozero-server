package com.zerozero.core.infrastructure.rabbitmq

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate

abstract class MessageProducer<T : BaseQueueProperty, R : Any>(
    private val rabbitTemplate: RabbitTemplate,
    private val queueProperty: T
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun publishMessage(request: R) {
        try {
            rabbitTemplate.convertAndSend(queueProperty.exchange, queueProperty.routingKey, request)
        } catch (e: Exception) {
            log.error("[MessageProducer] - publishMessage Failed", e)
        }
    }
}
