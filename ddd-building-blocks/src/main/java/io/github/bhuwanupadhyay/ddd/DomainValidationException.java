package io.github.bhuwanupadhyay.ddd;

// tag::code[]

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public final class DomainValidationException extends RuntimeException {

  private final List<DomainError> domainErrors;
  private final List<String> errorCodes;
  private final String message;

  public DomainValidationException(List<DomainError> errors) {
    this.domainErrors = Optional.ofNullable(errors).orElseGet(ArrayList::new);
    this.errorCodes = this.domainErrors.stream().map(DomainError::getErrorCode).collect(toList());
    this.message =
        String.format(
            "Found <%d> domain errors.\n--------\nDOMAIN_ERROR_CODES: [\n%s\n]\n--------\n",
            this.domainErrors.size(), String.join(",\n", this.errorCodes));
  }

  public List<DomainError> getDomainErrors() {
    return Collections.unmodifiableList(domainErrors);
  }

  public List<String> getErrorCodes() {
    return Collections.unmodifiableList(errorCodes);
  }

  public Integer getTotalErrors() {
    return this.domainErrors.size();
  }

  public boolean hasErrorCode(String errorCode) {
    return this.errorCodes.contains(errorCode);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
// end::code[]
