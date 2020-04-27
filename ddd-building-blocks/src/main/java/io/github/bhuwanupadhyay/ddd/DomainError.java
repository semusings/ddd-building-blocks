package io.github.bhuwanupadhyay.ddd;

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
  protected int toHashCode() {
    return Objects.hash(errorCode, errorMessage);
  }

  @Override
  public String toString() {
    return "DomainError{"
        + "violator='"
        + errorCode
        + '\''
        + ", message='"
        + errorMessage
        + '\''
        + '}';
  }
}
