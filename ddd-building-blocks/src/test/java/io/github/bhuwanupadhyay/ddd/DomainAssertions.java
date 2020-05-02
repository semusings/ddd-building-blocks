package io.github.bhuwanupadhyay.ddd;

// tag::code[]
import org.assertj.core.api.AbstractAssert;

import java.util.List;

public final class DomainAssertions {

  public static DomainValidationAssert assertThat(Runnable callback) {
    try {
      callback.run();
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

      if (this.actual == null) {
        failWithMessage("Domain errors does not occurred.");
      }

      List<DomainError> domainErrors = this.actual.getDomainErrors();

      if (domainErrors.isEmpty()) {
        failWithMessage("Domain validation does not have any errors.");
      }
      return this;
    }

    public DomainValidationAssert hasNoErrors() {
      if (this.actual != null) {
        failWithMessage(
            "Found total <%d> domain errors but expected zero errors.%s",
            this.actual.getTotalErrors(), this.actual.getMessage());
      }

      return this;
    }

    public DomainValidationAssert hasErrorCode(String errorCode) {
      this.hasErrors();

      long count =
          this.actual.getErrorCodes().stream().filter(error -> error.endsWith(errorCode)).count();

      if (count != 1) {
        failWithMessage(
            "%s exists <%d> times on errors but expected <1> times.%s",
            errorCode, count, this.actual.getMessage());
      }
      return this;
    }
  }
}
// end::code[]
