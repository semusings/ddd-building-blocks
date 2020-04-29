package io.github.bhuwanupadhyay.ddd;

// tag::code[]

import java.util.Objects;
import java.util.function.Supplier;

public final class DomainError extends ValueObject {

  public static final String DOMAIN_ERROR_CODE_SHOULD_NOT_BE_BLANK =
      "Domain error code should not be blank.";

  public static final String DOMAIN_ERROR_MESSAGE_SHOULD_NOT_BE_BLANK =
      "Domain error message should not be blank.";

  private final String errorCode;

  private final String errorMessage;

  public DomainError(String errorCode, String errorMessage) {

    DomainAsserts.begin()
        .raiseIfBlank(
            errorCode,
            create(getObjectName() + ".errorCode", DOMAIN_ERROR_CODE_SHOULD_NOT_BE_BLANK))
        .raiseIfBlank(
            errorMessage,
            create(getObjectName() + ".errorMessage", DOMAIN_ERROR_MESSAGE_SHOULD_NOT_BE_BLANK))
        .ifHasErrorsThrow();

    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public static Supplier<DomainError> create(final String errorCode, final String errorMessage) {
    return () -> new DomainError(errorCode, errorMessage);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DomainError that = (DomainError) o;
    return Objects.equals(errorCode, that.errorCode)
        && Objects.equals(errorMessage, that.errorMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errorCode, errorMessage);
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    return "DomainError{"
        + "errorCode='"
        + errorCode
        + '\''
        + ", errorMessage='"
        + errorMessage
        + '\''
        + '}';
  }
}
// end::code[]
