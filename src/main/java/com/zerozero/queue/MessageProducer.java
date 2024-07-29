package com.zerozero.queue;

import com.zerozero.configuration.ApplicationContextProvider;
import com.zerozero.core.application.BaseRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Log4j2
@RequiredArgsConstructor
public abstract class MessageProducer<T extends BaseQueueProperty, R extends BaseRequest> {

  private static final RabbitTemplate rabbitTemplate = ApplicationContextProvider.getBean("rabbitTemplate", RabbitTemplate.class);

  protected final T queueProperty;

  protected final R request;

  public boolean publishMessage() {
    try {
      rabbitTemplate.convertAndSend(queueProperty.getExchange(), queueProperty.getRoutingKey(), request);
    } catch (Exception e) {
      log.error("[MessageProducer] - publishMessage Failed", e);
      return false;
    }
    return true;
  }
}

