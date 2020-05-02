package io.github.bhuwanupadhyay.rtms.order.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.BadRequest;
import io.github.bhuwanupadhyay.rtms.orders.v1.CreateOrder;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrderPageList;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrderResource;
import io.github.bhuwanupadhyay.rtms.orders.v1.OrdersApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
class WebOrderApi implements OrdersApi {

  private static final String BEGIN = "BEGIN-----";
  private static final String END = "END-----";
  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();
  private final AppService appService;
  private final OrderQueryRepository queryRepository;
  private final ObjectMapper objectMapper;

  WebOrderApi(AppService appService, OrderQueryRepository queryRepository) {
    this.appService = appService;
    this.queryRepository = queryRepository;
    this.objectMapper = AppUtils.createObjectMapper();
  }

  @Override
  public Mono<ResponseEntity<OrderPageList>> getOrders(
      String filterJson,
      String sort,
      Integer pageNumber,
      Integer pageSize,
      ServerWebExchange exchange) {
    LOG.atInfo().log(BEGIN + "#getOrders");
    LOG.atInfo().log("Receive get orders http request.");
    LOG.atFinest().log("FilterJson: %s ", filterJson);
    LOG.atFinest().log("Sort: %s ", sort);
    LOG.atFinest().log("PageSize: %d ", pageSize);
    LOG.atFinest().log("PageNumber: %d ", pageNumber);
    try {
      OrderResource filterResource = null;
      if (filterJson != null) {
        filterResource = this.objectMapper.readValue(filterJson, OrderResource.class);
      }
      return Mono.just(
          ResponseEntity.ok(
              this.queryRepository.findAll(filterResource, pageSize, pageNumber, sort)));
    } catch (JsonProcessingException e) {
      LOG.atSevere().withCause(e).log("Error on converting filterJson to OrderResource");
      throw new BadRequest("NotAbleToConvertFilterJsonToOrderResource");
    } catch (Exception e) {
      LOG.atSevere().withCause(e).log("Error on get orders http request.");
      throw new BadRequest("NotAbleToGetOrders");
    } finally {
      LOG.atInfo().log(END + "#getOrders");
    }
  }

  @Override
  public Mono<ResponseEntity<OrderResource>> getOrdersByOrderId(
      String orderId, ServerWebExchange exchange) {
    LOG.atInfo().log(BEGIN + "#getOrdersByOrderId");
    LOG.atInfo().log("Receive get order by id http request.");
    LOG.atFinest().log("OrderId: %s ", orderId);
    try {
      return Mono.just(ResponseEntity.ok(this.queryRepository.findByOrderId(orderId)));
    } catch (Exception e) {
      LOG.atSevere().withCause(e).log("Error on get order by id http request.");
      throw new BadRequest("NotAbleToGetOrderById");
    } finally {
      LOG.atInfo().log(END + "#getOrdersByOrderId");
    }
  }

  @Override
  public Mono<ResponseEntity<OrderResource>> postOrders(
      Mono<CreateOrder> createOrder, ServerWebExchange exchange) {
    return createOrder
        .doFirst(
            () -> {
              LOG.atInfo().log(BEGIN + "#postOrders");
              LOG.atInfo().log("Receive create order http request.");
            })
        .map(appService::placeOrder)
        .map(orderId -> ResponseEntity.ok(queryRepository.findByOrderId(orderId.getId())))
        .doOnError(
            throwable -> {
              LOG.atSevere().withCause(throwable).log("Error on create order http request.");
              LOG.atInfo().log(END);
            })
        .doOnSuccess(response -> LOG.atInfo().log(END + "#postOrders"));
  }
}
