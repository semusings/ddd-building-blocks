package io.github.bhuwanupadhyay.rtms.order.v1;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

interface Streams {

  String IN = "rtmsOrdersIn";
  String OUT = "rtmsOrdersOut";

  @Input(IN)
  SubscribableChannel rtmsIn();

  @Output(OUT)
  MessageChannel rtmsOut();
}
