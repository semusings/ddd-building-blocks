package io.github.bhuwanupadhyay.rtms.order.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.bhuwanupadhyay.ddd.DomainError;
import io.github.bhuwanupadhyay.ddd.DomainValidationException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

class OrderTests {

  @Test
  void givenEmptyOrderId_thenOrderShouldNotCreated() {

    List<DomainError> domainErrors =
        assertThrows(DomainValidationException.class, () -> new Order(null)).getDomainErrors();

    assertThat(domainErrors)
        .hasSize(1)
        .first()
        .extracting("errorCode", "errorMessage")
        .containsSequence(Order.class.getName() + ".id", Order.ENTITY_ID_IS_REQUIRED);
  }

  private ListAssert<DomainError> assertThat(List<DomainError> domainErrors) {
    return Assertions.assertThat(domainErrors);
  }
}
