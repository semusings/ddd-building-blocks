package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.ValueObject;
import java.util.Objects;

public final class Customer extends ValueObject {
  private final String customerId;

  public Customer(String customerId) {
    this.customerId = customerId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Customer that = (Customer) o;
    return Objects.equals(customerId, that.customerId);
  }

  public String getCustomerId() {
    return customerId;
  }
}
