package io.github.bhuwanupadhyay.rtms.order.service;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.domain.Order;
import io.github.bhuwanupadhyay.rtms.order.domain.OrderId;
import io.github.bhuwanupadhyay.rtms.order.exception.AppDataException;
import io.github.bhuwanupadhyay.rtms.order.repository.OrderDomainRepository;
import io.github.bhuwanupadhyay.rtms.orders.v1.CreateOrder;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public final class AppService {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  private final OrderDomainRepository domainRepository;

  public AppService(OrderDomainRepository domainRepository) {
    this.domainRepository = domainRepository;
  }

  public void submitPayment(String orderId) {

    LOG.atInfo().log("Submitting payment for order %s", orderId);

    Order order = domainRepository.findOne(new OrderId(orderId)).orElseThrow(AppDataException::new);

    order.createPayment();

    LOG.atInfo().log("Created payment request for an order %s.", order.getId().getReference());
  }

  public Order placeOrder(CreateOrder createOrder) {
    OrderId orderId = new OrderId(UUID.randomUUID().toString());

    LOG.atInfo().log("Placing new order %s", orderId.getReference());

    Order order = new Order(orderId);

    order.creatOrder(createOrder.getProductId(), createOrder.getCustomerId(), 1);

    Order savedOrder = domainRepository.save(order);

    LOG.atInfo().log("Created new order %s", savedOrder.getId().getReference());

    return savedOrder;
  }
}
