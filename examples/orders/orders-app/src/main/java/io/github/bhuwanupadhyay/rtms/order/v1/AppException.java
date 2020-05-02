package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.ddd.DomainEvent;

abstract class AppException extends RuntimeException {

  private final String errorCode;
  private final String errorMessage;

  protected AppException(String errorCode, String errorMessage, Throwable cause) {
    super(String.format("[%s] - [%s]", errorCode, errorMessage), cause);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  protected AppException(String errorCode, String errorMessage) {
    this(errorCode, errorMessage, null);
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  static class BadRequest extends AppException {
    BadRequest(String errorCode) {
      super(errorCode, "");
    }
  }

  static class EntityNotFound extends AppException {
    EntityNotFound(String errorCode) {
      super(errorCode, "");
    }
  }

  static class DataAccessException extends AppException {
    DataAccessException(String errorCode, Throwable throwable) {
      super(errorCode, "", throwable);
    }

    DataAccessException(String errorCode) {
      super(errorCode, "");
    }
  }

  static class MessageStreamException extends AppException {
    MessageStreamException(DomainEvent event) {
      super(
          event.getEventClassName() + ".AvroSchemaNotDefined",
          "Avro schema not defined for domain event [" + event.getEventClassName() + "]");
    }
  }
}
