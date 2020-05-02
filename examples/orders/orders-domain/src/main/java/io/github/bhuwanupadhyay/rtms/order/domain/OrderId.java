package io.github.bhuwanupadhyay.rtms.order.domain;

import static io.github.bhuwanupadhyay.ddd.DomainAsserts.begin;

import io.github.bhuwanupadhyay.ddd.DomainError;
import io.github.bhuwanupadhyay.ddd.ValueObject;
import java.util.Objects;

public final class OrderId extends ValueObject {

  public static final String ORDER_ID_IS_REQUIRED = "OrderIdIsRequired";
  private final String id;

  public OrderId(String id) {
    begin(id).notNull(DomainError.create(this, ORDER_ID_IS_REQUIRED)).end();
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderId orderId = (OrderId) o;
    return Objects.equals(id, orderId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public String getId() {
    return this.id;
  }

  @Override
  public String toString() {
    return "OrderId{" + "reference='" + id + '\'' + '}';
  }
}
