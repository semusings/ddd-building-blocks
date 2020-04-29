package io.github.bhuwanupadhyay.rtms.order.v1;

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
    BadRequest(String errorCode, String errorMessage) {
      super(errorCode, errorMessage);
    }
  }

  static class EntityNotFound extends AppException {
    EntityNotFound(String errorCode, String errorMessage) {
      super(errorCode, errorMessage);
    }
  }

  static class MessageStreamException extends AppException {
    MessageStreamException(String errorCode, String errorMessage) {
      super(errorCode, errorMessage);
    }
  }
}
