package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.DomainError;
import io.github.bhuwanupadhyay.ddd.ValueObject;

import java.util.Objects;

import static io.github.bhuwanupadhyay.ddd.DomainAsserts.begin;

public final class OrderId extends ValueObject {

  public static final String REFERENCE_IS_REQUIRED = "ReferenceIsRequired";
  private final String reference;

  public OrderId(String reference) {
    begin(reference).notNull(DomainError.create(this, REFERENCE_IS_REQUIRED)).end();
    this.reference = reference;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderId orderId = (OrderId) o;
    return Objects.equals(reference, orderId.reference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }

  public String getReference() {
    return this.reference;
  }

  @Override
  public String toString() {
    return "OrderId{" + "reference='" + reference + '\'' + '}';
  }
}
