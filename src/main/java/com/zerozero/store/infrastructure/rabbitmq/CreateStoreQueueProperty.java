package com.zerozero.store.infrastructure.rabbitmq;

import com.zerozero.core.infrastructure.rabbitmq.BaseQueueProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("create-store-queue")
public class CreateStoreQueueProperty extends BaseQueueProperty {
}
