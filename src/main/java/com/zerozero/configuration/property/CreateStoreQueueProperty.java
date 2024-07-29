package com.zerozero.configuration.property;

import com.zerozero.queue.BaseQueueProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("create-store-queue")
public class CreateStoreQueueProperty extends BaseQueueProperty {
}
