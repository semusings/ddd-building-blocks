package io.github.bhuwanupadhyay.ddd;
// tag::code[]

public abstract class ValueObject {

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object o);

  public String getObjectName() {
    return this.getClass().getName();
  }
}
// end::code[]
