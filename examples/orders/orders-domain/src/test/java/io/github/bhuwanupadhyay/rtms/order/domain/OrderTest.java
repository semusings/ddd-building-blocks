package io.github.bhuwanupadhyay.rtms.order.domain;

import static io.github.bhuwanupadhyay.ddd.Entity.ENTITY_ID_IS_REQUIRED;
import static io.github.bhuwanupadhyay.ddd.test.DomainAssertions.assertThat;
import static io.github.bhuwanupadhyay.rtms.order.domain.OrderId.ORDER_ID_IS_REQUIRED;

import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  void givenEmptyOrderId_thenOrderShouldNotCreated() {
    assertThat(() -> new Order(null)).hasErrorCode(ENTITY_ID_IS_REQUIRED);

    assertThat(() -> new Order(new OrderId(null))).hasErrorCode(ORDER_ID_IS_REQUIRED);
  }

  @Test
  void givenNotEmptyOrderId_thenOrderShouldCreated() {
    assertThat(() -> new Order(new OrderId("O#0001"))).hasNoErrors();
  }

  @Test
  void product_Customer_Quantity_AreRequired_toPlaceOrder() {
    final Order order = new Order(new OrderId("O#0001"));

    assertThat(() -> order.placeOrder(null, null, null))
        .hasErrorCode("ProductIsRequired")
        .hasErrorCode("CustomerIsRequired")
        .hasErrorCode("QuantityIsRequired");
  }
}
