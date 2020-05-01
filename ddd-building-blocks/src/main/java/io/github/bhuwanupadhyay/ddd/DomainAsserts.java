package io.github.bhuwanupadhyay.ddd;

// tag::code[]

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class DomainAsserts {

  private DomainAsserts() {}

  public static <T> DomainAssert begin(T value) {
    return new DomainAssert(value);
  }

  public static <T> void raiseIfNull(T value, Supplier<DomainError> error) {
    begin(value).notNull(error).endAssertions();
  }

  public static void raiseIfBlank(String value, Supplier<DomainError> error) {
    begin(value).notBlank(error).endAssertions();
  }

  public static class DomainAssert<T> {
    private final List<DomainError> domainErrors = new ArrayList<>();

    private final T acutal;

    private DomainAssert(T value) {
      this.acutal = value;
    }

    public <T> DomainAssert notNull(Supplier<DomainError> error) {
      if (Objects.isNull(this.acutal)) {
        this.domainErrors.add(error.get());
      }
      return this;
    }

    public DomainAssert notBlank(Supplier<DomainError> error) {
      notNull(error);

      String s = (String) this.acutal;

      if (s.isBlank()) {
        this.domainErrors.add(error.get());
      }
      return this;
    }

    public <T> DomainAssert raiseIf(Predicate<T> predicate, Supplier<DomainError> error) {
      notNull(value, DomainError.create(value, "ValueForDomainAssertPredicateIsRequired"));

      if (predicate.test(value)) {
        this.domainErrors.add(error.get());
      }
      return this;
    }

    public void endAssertions() {
      if (this.domainErrors.isEmpty()) {
        return;
      }
      throw new DomainValidationException(this.domainErrors);
    }
  }
}
// end::code[]
