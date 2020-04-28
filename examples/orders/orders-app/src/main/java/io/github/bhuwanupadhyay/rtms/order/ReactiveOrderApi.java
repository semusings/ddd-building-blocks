package io.github.bhuwanupadhyay.rtms.order;

import io.github.bhuwanupadhyay.ddd.DomainRepository;
import io.github.bhuwanupadhyay.rtms.orders.v1.CreateOrder;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrderPageList;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrderResource;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrdersApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ReactiveOrderApi implements OrdersApi {

  private DomainRepository domainRepository;

  @Override
  public Mono<ResponseEntity<OrderPageList>> getOrders(
      String filterJson, String sort, ServerWebExchange exchange) {
    return null;
  }

  @Override
  public Mono<ResponseEntity<OrderResource>> getOrdersByOrderId(
      String orderId, ServerWebExchange exchange) {
    return null;
  }

  @Override
  public Mono<ResponseEntity<OrderResource>> postOrders(
      Mono<CreateOrder> createOrder, ServerWebExchange exchange) {
    return null;
  }
}
