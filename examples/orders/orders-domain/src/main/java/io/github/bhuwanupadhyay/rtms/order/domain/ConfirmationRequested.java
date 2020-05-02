package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.DomainEvent;

public class ConfirmationRequested extends DomainEvent {

  private final OrderId orderId;

  public ConfirmationRequested(OrderId orderId) {
    super(DomainEventType.OUTSIDE);
    this.orderId = orderId;
  }

  public OrderId getOrderId() {
    return orderId;
  }
}
