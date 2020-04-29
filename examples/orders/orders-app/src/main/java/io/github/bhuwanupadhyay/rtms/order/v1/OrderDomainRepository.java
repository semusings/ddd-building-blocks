package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.ddd.DomainEventPublisher;
import io.github.bhuwanupadhyay.ddd.DomainRepository;
import io.github.bhuwanupadhyay.rtms.order.domain.Order;
import io.github.bhuwanupadhyay.rtms.order.domain.OrderId;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class OrderDomainRepository extends DomainRepository<Order, OrderId> {

  OrderDomainRepository(DomainEventPublisher publisher) {
    super(publisher);
  }

  @Override
  public Optional<Order> findOne(OrderId orderId) {
    return Optional.empty();
  }

  @Override
  protected Order persist(Order entity) {
    return null;
  }
}
