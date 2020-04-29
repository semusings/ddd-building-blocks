package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.AggregateRoot;

public final class Order extends AggregateRoot<OrderId> {

  private String contactPhone;
  private String deliveryAddress;
  private String productId;
  private String customerId;
  private Integer quantity;

  public Order(OrderId id) {
    super(id);
  }

  public void creatOrder(String productId, String customerId, Integer quantity) {}

  public void createPayment() {}

  public Integer getQuantity() {
    return quantity;
  }

  public String getContactPhone() {
    return contactPhone;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getDeliveryAddress() {
    return deliveryAddress;
  }

  public String getProductId() {
    return productId;
  }
}
