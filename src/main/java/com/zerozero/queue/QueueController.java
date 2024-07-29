package com.zerozero.queue;

import com.zerozero.configuration.property.CreateStoreQueueProperty;
import com.zerozero.queue.store.CreateStoreMessageProducer;
import com.zerozero.queue.store.CreateStoreMessageProducer.CreateStoreMessageProducerRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QueueController {

  private final CreateStoreQueueProperty createStoreQueueProperty;

  @Hidden
  @PostMapping("/queue/store/create")
  public ResponseEntity<String> queueStore(@RequestBody CreateStoreMessageProducerRequest request) {
    if (request == null || !request.isValid()) {
      return ResponseEntity.badRequest().build();
    }
    CreateStoreMessageProducer producer = new CreateStoreMessageProducer(createStoreQueueProperty, request);
    boolean isPublished = producer.publishMessage();
    return ResponseEntity.status(isPublished ? 200 : 500).build();
  }

}
