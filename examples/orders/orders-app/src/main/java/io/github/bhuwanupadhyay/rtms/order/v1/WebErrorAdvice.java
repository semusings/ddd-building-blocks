package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.ddd.DomainException;
import io.github.bhuwanupadhyay.rtms.orders.v1.ErrorList;
import io.github.bhuwanupadhyay.rtms.orders.v1.ErrorMessage;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
class WebErrorAdvice {

  @ExceptionHandler
  Mono<ResponseEntity<ErrorList>> handleDomainErrors(DomainException e) {
    final List<ErrorMessage> errorMessages =
        e.getDomainErrors().stream()
            .map(
                domainError ->
                    new ErrorMessage()
                        .errorCode(domainError.getErrorCode())
                        .errorMessage(domainError.getErrorMessage()))
            .collect(Collectors.toList());

    return Mono.just(ResponseEntity.badRequest().body(new ErrorList().content(errorMessages)));
  }
}
