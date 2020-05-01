package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.AggregateRoot;
import io.github.bhuwanupadhyay.ddd.DomainAsserts;
import io.github.bhuwanupadhyay.ddd.DomainError;
import java.util.Optional;

public final class Order extends AggregateRoot<OrderId> implements OrderParams {

  private ContactPhone contactPhone;
  private DeliveryAddress deliveryAddress;
  private Product product;
  private Customer customer;
  private Quantity quantity;

  public Order(OrderId id) {
    super(id);
  }

  public void placeOrder(Product product, Customer customer, Quantity quantity) {

    DomainAsserts.begin(product)
        .notNull(DomainError.create(this, "ProductIsRequired"))
        .switchOn(customer)
        .notNull(DomainError.create(this, "CustomerIsRequired"))
        .switchOn(quantity)
        .notNull(DomainError.create(this, "QuantityIsRequired"))
        .end();

    this.product = product;
    this.customer = customer;
    this.quantity = quantity;
  }

  public void createPayment() {}

  @Override
  public Optional<String> getContactPhone() {
    return Optional.ofNullable(this.contactPhone).map(ContactPhone::getPhoneNumber);
  }

  @Override
  public Optional<String> getDeliveryAddress() {
    return Optional.ofNullable(this.deliveryAddress).map(DeliveryAddress::getAddress);
  }

  @Override
  public Optional<String> getCustomerId() {
    return Optional.ofNullable(this.customer).map(Customer::getCustomerId);
  }

  @Override
  public Optional<String> getProductId() {
    return Optional.ofNullable(this.product).map(Product::getProductId);
  }

  @Override
  public Optional<Integer> getQuantity() {
    return Optional.ofNullable(this.quantity).map(Quantity::getValue);
  }
}
