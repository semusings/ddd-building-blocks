package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.ddd.DomainValidationException;
import io.github.bhuwanupadhyay.rtms.orders.v1.ErrorList;
import io.github.bhuwanupadhyay.rtms.orders.v1.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
class WebErrorAdvice {

  @ExceptionHandler
  Mono<ResponseEntity<ErrorList>> handleDomainErrors(DomainValidationException e) {
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
