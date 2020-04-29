package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.ddd.DomainEvent;
import io.github.bhuwanupadhyay.ddd.DomainEventPublisher;
import io.github.bhuwanupadhyay.rtms.order.domain.PaymentRequested;
import io.github.bhuwanupadhyay.rtms.v1.SubmitPayment;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@EnableBinding(AppStream.class)
class AppDomainEventPublisher implements DomainEventPublisher {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();
  private final AppStream appStream;
  private final ApplicationEventPublisher eventPublisher;

  AppDomainEventPublisher(AppStream appStream, ApplicationEventPublisher eventPublisher) {
    this.appStream = appStream;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void publish(DomainEvent domainEvent) {
    LOG.atInfo().log("Received domain event %s", domainEvent.getEventClassName());

    if (domainEvent.isInsideContext()) {
      eventPublisher.publishEvent(domainEvent);
      LOG.atInfo().log("Published domain event inside context %s", domainEvent.getEventClassName());
    }

    if (domainEvent.isOutsideContext()) {
      appStream.rtmsOut().send(StreamMessageFactory.create(domainEvent));
      LOG.atInfo().log(
          "Published domain event outside context %s", domainEvent.getEventClassName());
    }
  }

  static class StreamMessageFactory {

    private static final Map<Class<? extends DomainEvent>, Class<? extends SpecificRecord>>
        NAMESPACES = Map.of(PaymentRequested.class, SubmitPayment.class);

    private StreamMessageFactory() {}

    static Message<?> create(DomainEvent domainEvent) {
      Map<String, Object> headers = new HashMap<>();

      Class<? extends SpecificRecord> namespace =
          Optional.ofNullable(NAMESPACES.get(domainEvent.getClass()))
              .orElseThrow(
                  () ->
                      new AppException.MessageStreamException(
                          "DomainEvent.AvroSchema.NotDefined",
                          String.format(
                              "Avro schema not defined for domain event %s",
                              domainEvent.getEventClassName())));
      headers.put("namespace", namespace.getName());

      return MessageBuilder.createMessage("", new MessageHeaders(headers));
    }
  }
}
