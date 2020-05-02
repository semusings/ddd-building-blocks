package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.ValueObject;

import java.util.Objects;

public final class ContactPerson extends ValueObject {

  private final String name;

  public ContactPerson(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ContactPerson that = (ContactPerson) o;
    return Objects.equals(name, that.name);
  }

  public String getName() {
    return name;
  }
}
