package io.github.bhuwanupadhyay.ddd.test;

import io.github.bhuwanupadhyay.ddd.DomainError;
import io.github.bhuwanupadhyay.ddd.DomainValidationException;
import org.assertj.core.api.AbstractAssert;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DomainAssertions {

  public static DomainValidationAssert assertThat(Supplier<?> callback) {
    try {
      callback.get();
      return new DomainValidationAssert(null);
    } catch (DomainValidationException e) {
      return new DomainValidationAssert(e);
    }
  }

  public static class DomainValidationAssert
      extends AbstractAssert<DomainValidationAssert, DomainValidationException> {

    private DomainValidationAssert(DomainValidationException exception) {
      super(exception, DomainValidationAssert.class);
    }

    private DomainValidationAssert hasErrors() {
      this.isNotNull();
      List<DomainError> domainErrors = this.actual.getDomainErrors();

      if (domainErrors.isEmpty()) {
        failWithMessage("Domain validation does not have any errors.");
      }
      return this;
    }

    public DomainValidationAssert hasNoErrors() {
      this.isNull();
      return this;
    }

    public DomainValidationAssert hasErrorCode(String errorCode) {
      return this.hasErrorCode(errorCode, 1);
    }

    private DomainValidationAssert hasErrorCode(String errorCode, int times) {
      this.hasErrors();

      long count =
          errorCodes().filter(Objects::nonNull).filter(error -> error.endsWith(errorCode)).count();

      if (count != times) {
        failWithMessage(
            "%s exists on errors <%d> times but expected <%d> times. ACTUAL_ERRORS: [%s]",
            errorCode, count, times, errorCodesAsString());
      }
      return this;
    }

    private Stream<String> errorCodes() {
      return this.actual.getDomainErrors().stream().map(DomainError::getErrorCode);
    }

    private String errorCodesAsString() {
      return errorCodes().collect(Collectors.joining(","));
    }
  }
}
