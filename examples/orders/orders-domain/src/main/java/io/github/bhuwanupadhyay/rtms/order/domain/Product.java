package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.ValueObject;

import java.util.Objects;

public final class Product extends ValueObject {

  private final String productId;

  public Product(String productId) {
    this.productId = productId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product that = (Product) o;
    return Objects.equals(productId, that.productId);
  }

  public String getProductId() {
    return productId;
  }
}
