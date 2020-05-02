package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.rtms.orders.v1.OrderResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class OrderRowMapper implements RowMapper<OrderResource> {

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
      throw new AppException.DataAccessException("ExceptionOccurredWhileMappingOrderResultSet", e);
    }
  }

  static final class OrderCriteria {
    private final List<Object> queryArgs = new ArrayList<>();
    private final List<Object> offsetArgs = new ArrayList<>();
    private final StringBuilder query = new StringBuilder(" ");
    private final StringBuilder sortQuery = new StringBuilder(" ");
    private final StringBuilder offSetQuery = new StringBuilder(" ");

    public OrderCriteria(OrderResource filter, String sort, Integer pageSize, Integer pageNumber) {
      if (filter != null) {
        buildFilters(filter);
      }
      if (sort != null) {
        buildSort(sort);
      }
      int startRow = 0;
      int size = 20;
      if (pageSize != null || pageNumber != null && pageSize > 0 && pageNumber > 0) {
        startRow = (pageNumber - 1) * pageSize;
        size = pageSize;
      }
      buildOffset(startRow, size);
    }

    private void buildOffset(int startRow, int size) {
      this.offSetQuery.append(" LIMIT ? OFFSET ?");
      this.offsetArgs.add(size);
      this.offsetArgs.add(startRow);
    }

    private void buildSort(String sort) {
      final String[] parts = sort.split(",");
      final String sortMode = parts[1];
      final String sortProperty = parts[0];
      if (parts.length == 2 && (sortMode.equals("ASC") || sortMode.equals("DESC"))) {
        sortQuery.append(" ORDER BY ");
        switch (sortProperty) {
          case "productId":
            sortQuery.append(PRODUCT_ID);
            break;
          case "id":
            sortQuery.append(ORDER_ID);
            break;
          case "customerId":
            sortQuery.append(CUSTOMER_ID);
            break;
          case "deliveryAddress":
            sortQuery.append(DELIVERY_ADDRESS);
            break;
          case "contactPhone":
            sortQuery.append(CONTACT_PHONE);
            break;
        }
        sortQuery.append(" ").append(sortMode).append(" ");
      }
    }

    private void buildFilters(OrderResource filter) {
      addFilter(ORDER_ID, filter.getId());
      addFilter(PRODUCT_ID, filter.getProductId());
      addFilter(CUSTOMER_ID, filter.getCustomerId());
      addFilter(QUANTITY, filter.getQuantity());
      addFilter(CONTACT_PHONE, filter.getContactPhone());
      addFilter(DELIVERY_ADDRESS, filter.getDeliveryAddress());
    }

    private void addFilter(String column, Object filterValue) {
      Optional.ofNullable(filterValue)
          .ifPresent(
              value -> {
                this.query.append(" AND " + column + "=?");
                this.queryArgs.add(value);
              });
    }

    public Object[] getCriteriaArgs() {
      return queryArgs.toArray();
    }

    public Object[] getFullCriteriaArgs() {
      final ArrayList<Object> objects = new ArrayList<>(queryArgs);
      objects.addAll(this.offsetArgs);
      return objects.toArray();
    }

    public String getCriteriaQuery() {
      return query.toString();
    }

    public String getFullCriteriaQuery() {
      return getCriteriaQuery() + sortQuery.toString() + offSetQuery.toString();
    }
  }
}
