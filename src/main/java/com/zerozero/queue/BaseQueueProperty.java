package com.zerozero.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseQueueProperty {

  protected String exchange;

  protected String queue;

  protected String routingKey;
}
