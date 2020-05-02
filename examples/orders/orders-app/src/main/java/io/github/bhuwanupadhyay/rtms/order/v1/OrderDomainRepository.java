package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.ddd.DomainEventPublisher;
import io.github.bhuwanupadhyay.ddd.DomainRepository;
import io.github.bhuwanupadhyay.rtms.order.domain.Order;
import io.github.bhuwanupadhyay.rtms.order.domain.OrderId;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.DataAccessException;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class OrderDomainRepository extends DomainRepository<Order, OrderId> {

  public static final String EMPTY = "";
  public static final int DEFAULT_QUANTITY = 1;
  private final JdbcTemplate jdbc;
  private final OrderQueries queries;

  OrderDomainRepository(DomainEventPublisher publisher, JdbcTemplate jdbc, OrderQueries queries) {
    super(publisher);
    this.jdbc = jdbc;
    this.queries = queries;
  }

  @Override
  public Optional<Order> findOne(OrderId orderId) {
    final Map<String, Object> result =
        this.jdbc.queryForMap(queries.getOrderById(), orderId.getId());

    return Optional.ofNullable(result)
        .map(map -> new Order(new OrderId((String) map.get("orderId"))));
  }

  @Override
  protected void persist(Order entity) {
    try {
      jdbc.update(
          queries.getSaveOrder(),
          entity.getId().getId(),
          entity.getProductId().orElse(EMPTY),
          entity.getCustomerId().orElse(EMPTY),
          entity.getDeliveryAddress().orElse(EMPTY),
          entity.getContactPhone().orElse(EMPTY),
          entity.getQuantity().orElse(DEFAULT_QUANTITY));
    } catch (Exception e) {
      throw new DataAccessException("ExceptionOccurredWhileSavingOrder", e);
    }
  }
}
