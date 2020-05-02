package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.DomainEvent;

public class ShippingRequested extends DomainEvent {

  private final OrderId orderId;

  public ShippingRequested(OrderId orderId) {
    super(DomainEventType.OUTSIDE);
    this.orderId = orderId;
  }

  public OrderId getOrderId() {
    return orderId;
  }
}
