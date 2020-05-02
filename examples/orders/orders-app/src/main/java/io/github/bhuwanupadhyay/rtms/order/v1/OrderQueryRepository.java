package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.EntityNotFound;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrderPageList;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrderResource;
import io.github.bhuwanupadhyay.rtms.orders.v1.PageResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
class OrderQueryRepository {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  private final JdbcTemplate jdbc;
  private final OrderQueries queries;

  public OrderQueryRepository(JdbcTemplate jdbc, OrderQueries queries) {
    this.jdbc = jdbc;
    this.queries = queries;
  }

  public OrderResource findByOrderId(String orderId) {
    LOG.atInfo().log("Find order by order id %s", orderId);
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

  public OrderPageList findAll(
      OrderResource filterResource, Integer pageSize, Integer pageNumber, String sort) {
    LOG.atInfo().log("Find all orders with page size %d, page number %d", pageSize, pageNumber);
    final Long rowsCount =
        jdbc.queryForObject(queries.getCountOrders(), new Object[] {}, Long.class);

    int startRow = pageNumber * pageSize;
    int endRow = startRow + pageSize;

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

    int totalPages = (int) (rowsCount / pageSize);
    if (rowsCount % pageSize != 0) {
      totalPages++;
    }

    return new OrderPageList()
        .page(
            new PageResource()
                .totalElements(rowsCount)
                .totalPages(totalPages)
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .sort(sort))
        .content(orderResources);
  }
}
