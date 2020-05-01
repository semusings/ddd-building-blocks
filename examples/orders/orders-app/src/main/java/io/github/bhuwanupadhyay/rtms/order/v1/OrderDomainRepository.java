package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.ddd.DomainEventPublisher;
import io.github.bhuwanupadhyay.ddd.DomainRepository;
import io.github.bhuwanupadhyay.rtms.order.domain.Order;
import io.github.bhuwanupadhyay.rtms.order.domain.OrderId;
import io.github.bhuwanupadhyay.rtms.order.v1.persistance.SpringDataOrderRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class OrderDomainRepository extends DomainRepository<Order, OrderId> {

  private final SpringDataOrderRepository repository;

  OrderDomainRepository(DomainEventPublisher publisher, SpringDataOrderRepository repository) {
    super(publisher);
    this.repository = repository;
  }

  @Override
  public Optional<Order> findOne(OrderId orderId) {
    return repository.findById();
  }

  @Override
  protected Order persist(Order entity) {
    return null;
  }
}
