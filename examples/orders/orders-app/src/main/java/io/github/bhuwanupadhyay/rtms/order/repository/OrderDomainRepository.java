package io.github.bhuwanupadhyay.rtms.order.repository;

import io.github.bhuwanupadhyay.ddd.DomainEventPublisher;
import io.github.bhuwanupadhyay.ddd.DomainRepository;
import io.github.bhuwanupadhyay.rtms.order.domain.Order;
import io.github.bhuwanupadhyay.rtms.order.domain.OrderId;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public final class OrderDomainRepository extends DomainRepository<Order, OrderId> {

  public OrderDomainRepository(DomainEventPublisher publisher) {
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
