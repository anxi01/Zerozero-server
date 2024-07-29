package com.zerozero.queue.store;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.queue.MessageConsumer;
import com.zerozero.queue.store.CreateStoreMessageConsumer.CreateStoreMessageProducerRequest;
import com.zerozero.store.application.CreateStoreMongoUseCase;
import com.zerozero.store.application.CreateStoreMongoUseCase.CreateStoreMongoRequest;
import com.zerozero.store.application.CreateStoreMongoUseCase.CreateStoreMongoResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional
public class CreateStoreMessageConsumer implements MessageConsumer<CreateStoreMessageProducerRequest> {

  private final CreateStoreMongoUseCase createStoreMongoUseCase;

  @Override
  @RabbitListener(queues = "${create-store-queue.queue}")
  public void consumeMessage(CreateStoreMessageProducerRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[CreateStoreMessageConsumer] Invalid CreateStoreMessageProducerRequest");
      return;
    }
    CreateStoreMongoResponse createStoreMongoResponse = createStoreMongoUseCase.execute(
        CreateStoreMongoRequest.builder()
            .storeId(request.getStoreId())
            .build());
    if (createStoreMongoResponse == null) {
      log.error("[CreateStoreMessageConsumer] Create Store Region Response is null");
    }
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
