package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.DomainAsserts;
import io.github.bhuwanupadhyay.ddd.DomainError;
import io.github.bhuwanupadhyay.ddd.ValueObject;
import java.util.Objects;

public final class Quantity extends ValueObject {

  private final Integer value;

  public Quantity(Integer value) {

    DomainAsserts.begin(value)
        .raiseIf(this::isLessThanOne, DomainError.create(this, "QuantityValueShouldBePositive"))
        .end();

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

  private boolean isLessThanOne(Integer quantity) {
    return quantity < 1;
  }
}
