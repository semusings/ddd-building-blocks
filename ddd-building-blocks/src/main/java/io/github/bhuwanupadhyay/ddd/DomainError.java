package io.github.bhuwanupadhyay.ddd;

// tag::code[]

import java.util.Objects;
import java.util.function.Supplier;

public final class DomainError extends ValueObject {

  private static final String ENTITY = "Entity";
  private static final String DOMAIN_EVENT = "DomainEvent";
  private static final String VALUE_OBJECT = "ValueObject";
  private static final String DOMAIN_REPOSITORY = "DomainRepository";
  private static final String AGGREGATE_ROOT = "AggregateRoot";
  private static final String DOMAIN_SERVICE = "DomainService";
  private final String errorCode;

  private final String errorMessage;

  private DomainError(String errorCode, String errorMessage) {

    DomainAsserts.begin(errorCode)
        .notBlank(create(this, "ErrorCodeIsRequired"))
        .switchOn(errorMessage)
        .notBlank(create(this, "ErrorMessageIsRequired"))
        .end();

    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public static <T extends DomainEvent> Supplier<DomainError> create(
      final T type, final String code) {
    return () -> createError(type, DOMAIN_EVENT, code);
  }

  public static <T extends Entity<?>> Supplier<DomainError> create(
      final T type, final String code) {
    return () -> createError(type, ENTITY, code);
  }

  public static <T extends ValueObject> Supplier<DomainError> create(
      final T type, final String code) {
    return () -> createError(type, VALUE_OBJECT, code);
  }

  public static <T extends DomainRepository<?, ?>> Supplier<DomainError> create(
      final T type, final String code) {
    return () -> createError(type, DOMAIN_REPOSITORY, code);
  }

  public static <T extends AggregateRoot<?>> Supplier<DomainError> create(
      final T type, final String code) {
    return () -> createError(type, AGGREGATE_ROOT, code);
  }

  public static <T extends DomainService> Supplier<DomainError> create(
      final T type, final String code) {
    return () -> createError(type, DOMAIN_SERVICE, code);
  }

  private static DomainError createError(Object o, String source, String code) {
    final String errorCode = o.getClass().getName() + "." + code;
    return new DomainError(errorCode, "Domain violation on " + source + " [ " + errorCode + " ]");
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
