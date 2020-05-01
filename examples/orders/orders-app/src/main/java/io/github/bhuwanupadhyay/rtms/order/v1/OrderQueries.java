package io.github.bhuwanupadhyay.rtms.order.v1;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
class OrderQueries {
  private final String orderById;
  private final String orders;
  private final String countOrders;

  OrderQueries() {
    this.orders = "";
    this.orderById = "";
    this.countOrders = "";
  }
}
