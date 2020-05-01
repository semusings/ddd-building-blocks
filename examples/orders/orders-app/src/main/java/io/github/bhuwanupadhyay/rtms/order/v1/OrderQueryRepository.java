package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.rtms.orders.v1.OrderResource;
import org.springframework.stereotype.Repository;

@Repository
class OrderQueryRepository {

  public OrderResource findByOrderId(String orderId) {
    return null;
  }
}
