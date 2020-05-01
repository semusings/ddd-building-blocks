package io.github.bhuwanupadhyay.rtms.order.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.bhuwanupadhyay.ddd.test.DomainAssertions;
import org.junit.jupiter.api.Test;

class OrderIdTest {

  @Test
  void canEqualsByValues() {
    final String reference = "order1";

    OrderId order1 = new OrderId(reference);
    OrderId order2 = new OrderId(reference);

    assertEquals(order1, order2);
  }

  @Test
  void referenceIsRequired() {
    DomainAssertions.assertThat(() -> new OrderId(null)).hasErrorCode(OrderId.ORDER_ID_IS_REQUIRED);
  }
}
