package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.DomainEvent;

public class PaymentRequested extends DomainEvent {

  public PaymentRequested() {
    super(DomainEventType.OUTSIDE);
  }
}
