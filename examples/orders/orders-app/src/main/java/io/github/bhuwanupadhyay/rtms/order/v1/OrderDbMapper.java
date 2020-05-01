package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.domain.Order;
import io.github.bhuwanupadhyay.rtms.order.domain.OrderId;
import org.springframework.stereotype.Component;

@Component
class OrderDbMapper {
  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  public Order toOrder(OrderDb orderDb) {
    final Order order = new Order(new OrderId(orderDb.getOrderId()));
    return order;
  }

  public OrderDb toOrderData(Order entity) {
    final OrderDb orderDb = new OrderDb();
    orderDb.setOrderId(entity.getId().getId());
    return orderDb;
  }
}
