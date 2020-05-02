package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.ddd.DomainEventPublisher;
import io.github.bhuwanupadhyay.ddd.DomainRepository;
import io.github.bhuwanupadhyay.rtms.order.domain.Order;
import io.github.bhuwanupadhyay.rtms.order.domain.OrderId;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class OrderDomainRepository extends DomainRepository<Order, OrderId> {

  private final SpringDataOrderRepository dataRepository;
  private final OrderDbMapper dataMapper;

  OrderDomainRepository(
      DomainEventPublisher publisher,
      SpringDataOrderRepository dataRepository,
      OrderDbMapper dataMapper) {
    super(publisher);
    this.dataRepository = dataRepository;
    this.dataMapper = dataMapper;
  }

  @Override
  public Optional<Order> findOne(OrderId orderId) {
    return dataRepository.findById(orderId.getId()).map(dataMapper::toOrder);
  }

  @Override
  protected void persist(Order entity) {
    try {
      dataRepository.save(dataMapper.toOrderData(entity));
    } catch (Exception e) {
      throw new DataAccessException("ExceptionOccurredWhileSavingOrder", e);
    }
  }
}
