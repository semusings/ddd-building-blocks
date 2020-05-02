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
  private final OrderResourceMapper rowMapper;

  public OrderQueryRepository(JdbcTemplate jdbc, OrderQueries queries) {
    this.jdbc = jdbc;
    this.queries = queries;
    this.rowMapper = new OrderResourceMapper();
  }

  public OrderResource findByOrderId(String orderId) {
    LOG.atInfo().log("Find order by order id %s", orderId);
    return Optional.ofNullable(
            jdbc.queryForObject(queries.getOrderById(), new Object[] {orderId}, rowMapper))
        .orElseThrow(() -> new EntityNotFound("OrderIdDoesNotExists"));
  }

  public OrderPageList findAll(
      OrderResource filterResource, Integer pageSize, Integer pageNumber, String sort) {
    LOG.atInfo().log("Find all orders with page size %d, page number %d", pageSize, pageNumber);
    final Object[] args = {};

    final Long rowsCount = jdbc.queryForObject(queries.getCountOrders(), args, Long.class);

    int startRow = pageNumber * pageSize;
    int endRow = startRow + pageSize;

    final List<OrderResource> items = jdbc.query(queries.getOrders(), args, rowMapper);

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
        .content(items);
  }

  private static final class OrderResourceMapper implements RowMapper<OrderResource> {

    public OrderResource mapRow(ResultSet rs, int rowNum) throws SQLException {
      try {
        return new OrderResource()
            .id(rs.getString("order_id"))
            .customerId(rs.getString("customer_id"))
            .productId(rs.getString("product_id"))
            .contactPhone(rs.getString("contact_phone"))
            .deliveryAddress(rs.getString("delivery_address"))
            .quantity(rs.getInt("quantity"));
      } catch (SQLException e) {
        throw new AppException.DataAccessException(
            "ExceptionOccurredWhileMappingOrderResultSet", e);
      }
    }
  }
}
