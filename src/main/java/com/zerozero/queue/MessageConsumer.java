package com.zerozero.queue;

import com.zerozero.core.application.BaseRequest;

public interface MessageConsumer<R extends BaseRequest> {

  void consumeMessage(R request);
}
