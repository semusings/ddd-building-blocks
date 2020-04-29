package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.ddd.DomainEvent;
import io.github.bhuwanupadhyay.rtms.order.domain.PaymentRequested;
import io.github.bhuwanupadhyay.rtms.v1.SubmitPayment;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class StreamMessageFactory {

  private static final Map<Class<? extends DomainEvent>, Class<? extends SpecificRecord>>
      NAMESPACES = Map.of(PaymentRequested.class, SubmitPayment.class);

  private StreamMessageFactory() {}

  static Message<?> create(DomainEvent domainEvent) {
    Map<String, Object> headers = new HashMap<>();

    Class<? extends SpecificRecord> namespace =
        Optional.ofNullable(NAMESPACES.get(domainEvent.getClass()))
            .orElseThrow(AppIntegrationException::new);
    headers.put("namespace", namespace.getName());

    return MessageBuilder.createMessage("", new MessageHeaders(headers));
  }
}
