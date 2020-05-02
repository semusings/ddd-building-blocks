package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.DataAccessException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;

@Component
class OrderQueries {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  private final String orderById;
  private final String saveOrder;
  private final String orders;
  private final String countOrders;

  OrderQueries() {
    this.orders = readSQLQuery("queries/get-orders.sql");
    this.orderById = readSQLQuery("queries/get-order-by-id.sql");
    this.countOrders = readSQLQuery("queries/count-orders.sql");
    this.saveOrder = readSQLQuery("queries/save-order.sql");
  }

  private String readSQLQuery(String path) {
    try {
      final String sqlQuery =
          FileCopyUtils.copyToString(
              new InputStreamReader(new ClassPathResource(path).getInputStream()));
      LOG.atInfo().log("Successfully read sql query file %s from classpath.", path);
      return sqlQuery;
    } catch (IOException e) {
      LOG.atSevere().withCause(e).log("Unable to load sql query file %s in classpath.", path);
      throw new DataAccessException("", e);
    }
  }

  public String getOrderById() {
    return orderById;
  }

  public String getOrders() {
    return orders;
  }

  public String getCountOrders() {
    return countOrders;
  }

  public String getSaveOrder() {
    return saveOrder;
  }
}
