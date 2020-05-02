package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.EntityNotFound;
import io.github.bhuwanupadhyay.rtms.order.v1.OrderRowMapper.OrderCriteria;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrderPageList;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrderResource;
import io.github.bhuwanupadhyay.rtms.orders.v1.PageResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class OrderQueryRepository {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  private final JdbcTemplate jdbc;
  private final OrderQueries queries;
  private final OrderRowMapper rowMapper;

  public OrderQueryRepository(JdbcTemplate jdbc, OrderQueries queries, OrderRowMapper rowMapper) {
    this.jdbc = jdbc;
    this.queries = queries;
    this.rowMapper = rowMapper;
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

    final OrderCriteria criteria = new OrderCriteria(filterResource, sort, pageSize, pageNumber);

    final String countOrdersSql = queries.getCountOrders() + criteria.getCriteriaQuery();

    final Long rowsCount = jdbc.queryForObject(countOrdersSql, criteria.getCriteriaArgs(), Long.class);

    final String getOrdersSql = queries.getOrders() + criteria.getFullCriteriaQuery();

    final List<OrderResource> items = jdbc.query(getOrdersSql, criteria.getFullCriteriaArgs(), rowMapper);

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
}
