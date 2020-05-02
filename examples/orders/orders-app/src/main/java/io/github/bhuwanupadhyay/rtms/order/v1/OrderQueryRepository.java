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
import java.util.ArrayList;
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

    final OrderResourceMapper.WhereClause whereClause =
        new OrderResourceMapper.WhereClause(filterResource);

    final String countOrdersSql = queries.getCountOrders() + whereClause.getQuery();

    final Long rowsCount = jdbc.queryForObject(countOrdersSql, whereClause.getArgs(), Long.class);

    int startRow = pageNumber * pageSize;
    int endRow = startRow + pageSize;

    final String getOrdersSql = queries.getOrders() + whereClause.getQuery();
    final List<OrderResource> items = jdbc.query(getOrdersSql, whereClause.getArgs(), rowMapper);

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

    private static final String ORDER_ID = "order_id";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String CONTACT_PHONE = "contact_phone";
    private static final String DELIVERY_ADDRESS = "delivery_address";
    private static final String QUANTITY = "quantity";

    public OrderResource mapRow(ResultSet rs, int rowNum) throws SQLException {
      try {
        return new OrderResource()
            .id(rs.getString(ORDER_ID))
            .customerId(rs.getString(CUSTOMER_ID))
            .productId(rs.getString(PRODUCT_ID))
            .contactPhone(rs.getString(CONTACT_PHONE))
            .deliveryAddress(rs.getString(DELIVERY_ADDRESS))
            .quantity(rs.getInt(QUANTITY));
      } catch (SQLException e) {
        throw new AppException.DataAccessException(
            "ExceptionOccurredWhileMappingOrderResultSet", e);
      }
    }

    private static final class WhereClause {
      private final Object[] args;
      private final String query;

      public WhereClause(OrderResource filter) {
        StringBuilder query = new StringBuilder();
        List<Object> list = new ArrayList<>();
        if (filter != null) {
          addFilter(query, list, ORDER_ID, filter.getId());
          addFilter(query, list, PRODUCT_ID, filter.getProductId());
          addFilter(query, list, CUSTOMER_ID, filter.getCustomerId());
          addFilter(query, list, QUANTITY, filter.getQuantity());
          addFilter(query, list, CONTACT_PHONE, filter.getContactPhone());
          addFilter(query, list, DELIVERY_ADDRESS, filter.getDeliveryAddress());
        }
        this.args = list.toArray();
        this.query = query.toString();
      }

      private void addFilter(
          StringBuilder query, List<Object> args, String column, Object filterValue) {
        Optional.ofNullable(filterValue)
            .ifPresent(
                value -> {
                  query.append(" AND " + column + "=?");
                  args.add(value);
                });
      }

      public Object[] getArgs() {
        return args;
      }

      public String getQuery() {
        return query;
      }
    }
  }
}
