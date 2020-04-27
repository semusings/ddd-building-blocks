package io.github.bhuwanupadhyay.ddd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class DomainAsserts {

  private DomainAsserts() {}

  public static DomainAssert begin() {
    return new DomainAssert();
  }

  public static <T> void raiseIfNull(T value, Supplier<DomainError> error) {
    begin().raiseIfNull(value, error).ifHasErrorsThrow();
  }

  public static class DomainAssert {
    private final List<DomainError> domainErrors = new ArrayList<>();

    private DomainAssert() {}

    public <T> DomainAssert raiseIfNull(T value, Supplier<DomainError> error) {
      if (Objects.isNull(value)) {
        this.domainErrors.add(error.get());
      }
      return this;
    }

    public DomainAssert raiseIfBlank(String value, Supplier<DomainError> error) {
      raiseIfNull(value, error);

      if (value != null && value.isBlank()) {
        this.domainErrors.add(error.get());
      }
      return this;
    }

    public void ifHasErrorsThrow() {
      if (this.domainErrors.isEmpty()) {
        return;
      }
      throw new DomainValidationException(this.domainErrors);
    }
  }
}
