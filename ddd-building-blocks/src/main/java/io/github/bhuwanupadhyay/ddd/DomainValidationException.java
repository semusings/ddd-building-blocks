package io.github.bhuwanupadhyay.ddd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DomainValidationException extends RuntimeException {

  private final List<DomainError> domainErrors = new ArrayList<>();

  public DomainValidationException(List<DomainError> domainErrors) {
    super("[" + domainErrors.size() + "] domain errors.");
    this.domainErrors.addAll(domainErrors);
  }

  public List<DomainError> getDomainErrors() {
    return Collections.unmodifiableList(domainErrors);
  }
}
