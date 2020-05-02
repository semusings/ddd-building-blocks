package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.DomainEvent;

public class OrderPlaced extends DomainEvent {

  private final Product product;
  private final Quantity quantity;
  private final OrderId orderId;

  public OrderPlaced(OrderId orderId, Product product, Quantity quantity) {
    super(DomainEventType.OUTSIDE);
    this.product = product;
    this.quantity = quantity;
    this.orderId = orderId;
  }

  public OrderId getOrderId() {
    return orderId;
  }

  public Product getProduct() {
    return product;
  }

  public Quantity getQuantity() {
    return quantity;
  }
}
