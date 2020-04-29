package io.github.bhuwanupadhyay.rtms.order.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrderIdTest {

  @Test
  void canEqualsByValues() {
    final String reference = "order1";

    OrderId order1 = new OrderId(reference);
    OrderId order2 = new OrderId(reference);

    assertEquals(order1, order2);
  }
}
