package io.github.bhuwanupadhyay.rtms.order.integration;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.ddd.DomainEvent;
import io.github.bhuwanupadhyay.ddd.DomainEventPublisher;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Streams.class)
public class AppDomainEventPublisher implements DomainEventPublisher {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();
  private final Streams streams;
  private final ApplicationEventPublisher events;

  public AppDomainEventPublisher(Streams streams, ApplicationEventPublisher events) {
    this.streams = streams;
    this.events = events;
  }

  @Override
  public void publish(DomainEvent domainEvent) {
    LOG.atInfo().log("Received domain event %s", domainEvent.getEventClassName());

    if (domainEvent.isInsideContext()) {
      events.publishEvent(domainEvent);
      LOG.atInfo().log("Published domain event inside context %s", domainEvent.getEventClassName());
    }

    if (domainEvent.isOutsideContext()) {
      streams.rtmsOut().send(StreamMessageFactory.create(domainEvent));
      LOG.atInfo().log(
          "Published domain event outside context %s", domainEvent.getEventClassName());
    }
  }
}
