package io.github.bhuwanupadhyay.rtms.order.domain;

import io.github.bhuwanupadhyay.ddd.ValueObject;

import java.util.Objects;

public final class ContactPhone extends ValueObject {

  private final String phoneNumber;

  public ContactPhone(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public int hashCode() {
    return Objects.hash(phoneNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ContactPhone that = (ContactPhone) o;
    return Objects.equals(phoneNumber, that.phoneNumber);
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}
