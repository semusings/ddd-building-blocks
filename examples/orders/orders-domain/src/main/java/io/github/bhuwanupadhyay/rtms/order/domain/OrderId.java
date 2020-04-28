package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.DomainAsserts;
import io.github.bhuwanupadhyay.ddd.DomainError;
import io.github.bhuwanupadhyay.ddd.ValueObject;
import java.util.Objects;

public final class OrderId extends ValueObject {

  private final String reference;

  public OrderId(String reference) {
    DomainAsserts.raiseIfBlank(
        reference,
        DomainError.create(getObjectName() + ".reference", "OrderId reference is required."));
    this.reference = reference;
  }

  @Override
  protected int toHashCode() {
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
