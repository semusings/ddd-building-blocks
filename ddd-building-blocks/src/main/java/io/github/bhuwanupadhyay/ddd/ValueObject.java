package io.github.bhuwanupadhyay.ddd;
// tag::code[]

public abstract class ValueObject {

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object o);
}
// end::code[]
