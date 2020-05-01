package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.DomainAsserts;
import io.github.bhuwanupadhyay.ddd.DomainError;
import io.github.bhuwanupadhyay.ddd.ValueObject;

import java.util.Objects;

public final class Quantity extends ValueObject {

  private final Integer value;

  public Quantity(Integer value) {

  	DomainAsserts.begin()
        .raiseIfNull(value, DomainError.create(this, "QuantityValueIsRequired"))
        .raiseIf(() -> value < 1,  DomainError.create(this, "QuantityValueShouldBePositive"))
        .endAssertions();

    this.value = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Quantity that = (Quantity) o;
    return Objects.equals(value, that.value);
  }

  public Integer getValue() {
    return value;
  }
}
