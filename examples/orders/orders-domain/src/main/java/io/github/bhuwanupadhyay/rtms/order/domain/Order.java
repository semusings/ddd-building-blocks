package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.AggregateRoot;

public final class Order extends AggregateRoot<OrderId> {

  public Order(OrderId orderId) {
    super(orderId);
  }

  public void creatOrder(String productId, String customerId, Integer quantity) {}

  public void createPayment() {}
}
