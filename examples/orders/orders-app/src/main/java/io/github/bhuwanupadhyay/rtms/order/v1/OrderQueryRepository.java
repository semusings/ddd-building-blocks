package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.EntityNotFound;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrderPageList;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrderResource;
import io.github.bhuwanupadhyay.rtms.orders.v1.PageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class OrderQueryRepository {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  private final JdbcTemplate jdbc;
  private final OrderQueries queries;

  public OrderResource findByOrderId(String orderId) {
    return jdbc.query(
        queries.getOrderById(),
        new Object[] {orderId},
        rs -> {
          return Optional.of(rs.next())
              .filter(Boolean::booleanValue)
              .map(ignored -> new OrderResource())
              .orElseThrow(() -> new EntityNotFound("OrderIdDoesNotExists"));
        });
  }

  public OrderPageList findAll(OrderResource filterResource, String sort) {
    final Long rowsCount =
        jdbc.queryForObject(queries.getCountOrders(), new Object[] {}, Long.class);

    final List<OrderResource> orderResources =
        jdbc.query(
            queries.getOrders(),
            new Object[] {},
            new RowMapper<>() {
              @Override
              public OrderResource mapRow(ResultSet rs, int rowNum) throws SQLException {
                return Optional.of(rs.next())
                    .filter(Boolean::booleanValue)
                    .map(ignored -> new OrderResource())
                    .orElseGet(() -> null);
              }
            });
    return new OrderPageList()
        .page(new PageResource().totalElements(rowsCount))
        .content(orderResources);
  }
}
