package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.ValueObject;
import java.util.Objects;

public final class DeliveryAddress extends ValueObject {

  private final String address;

  public DeliveryAddress(String address) {
    this.address = address;
  }

  @Override
  public int hashCode() {
    return Objects.hash(address);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DeliveryAddress that = (DeliveryAddress) o;
    return Objects.equals(address, that.address);
  }

  public String getAddress() {
    return address;
  }
}
