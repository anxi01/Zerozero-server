package com.zerozero.queue.store;

import com.zerozero.configuration.property.CreateStoreQueueProperty;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.queue.MessageProducer;
import com.zerozero.queue.store.CreateStoreMessageProducer.CreateStoreMessageProducerRequest;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CreateStoreMessageProducer extends MessageProducer<CreateStoreQueueProperty, CreateStoreMessageProducerRequest> {

  private CreateStoreMessageProducer() {
    super(null, null);
  }

  public CreateStoreMessageProducer(CreateStoreQueueProperty queueProperty,
      CreateStoreMessageProducerRequest request) {
    super(queueProperty, request);
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreateStoreMessageProducerRequest implements BaseRequest {

    private UUID storeId;

    @Override
    public boolean isValid() {
      return storeId != null;
    }
  }
}
