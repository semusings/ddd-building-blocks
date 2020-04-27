package io.github.bhuwanupadhyay.ddd;

import java.util.Objects;

public abstract class ValueObject {

  @Override
  public int hashCode() {
    return this.toHashCode();
  }

  /**
   * This method will determine hash code of your Value Object attributes. {@link DomainError}, is
   * one of value object created by using {@link ValueObject}.
   *
   * @return hash code of an object
   */
  protected abstract int toHashCode();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ValueObject valueObject = (ValueObject) o;
    return Objects.equals(this, valueObject);
  }

  public String getObjectName() {
    return this.getClass().getName();
  }
}
