package io.github.bhuwanupadhyay.rtms.order.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Streams {

  String IN = "rtmsIn";
  String OUT = "rtmsOut";

  @Input(IN)
  SubscribableChannel lgInput();

  @Output(OUT)
  MessageChannel lgOutputNotifications();
}
