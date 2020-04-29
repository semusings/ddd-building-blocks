package io.github.bhuwanupadhyay.rtms.order.integration;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Streams {

  String IN = "rtmsOrdersIn";
  String OUT = "rtmsOrdersOut";

  @Input(IN)
  SubscribableChannel rtmsIn();

  @Output(OUT)
  MessageChannel rtmsOut();
}
