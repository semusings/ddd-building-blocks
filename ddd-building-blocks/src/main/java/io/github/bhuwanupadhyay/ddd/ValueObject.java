package io.github.bhuwanupadhyay.ddd;

// tag::code[]

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
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  public String getObjectName() {
    return this.getClass().getName();
  }
}
// end::code[]
