package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.domain.*;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.EntityNotFound;
import io.github.bhuwanupadhyay.rtms.orders.v1.CreateOrder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class AppService {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  private final OrderDomainRepository domainRepository;

  AppService(OrderDomainRepository domainRepository) {
    this.domainRepository = domainRepository;
  }

  void submitPayment(String orderId) {

    LOG.atInfo().log("Submitting payment for order %s", orderId);

    Order order =
        domainRepository
            .findOne(new OrderId(orderId))
            .orElseThrow(
                () ->
                    new EntityNotFound(
                        "Order.By.Id.NotFound",
                        String.format("Order by id %s not found.", orderId)));

    order.createPayment();

    LOG.atInfo().log("Created payment request for an order %s.", order.getId().getReference());
  }

  Order placeOrder(CreateOrder createOrder) {
    OrderId orderId = new OrderId(UUID.randomUUID().toString());

    LOG.atInfo().log("Placing new order %s", orderId.getReference());

    Order order = new Order(orderId);

    Quantity quantity = new Quantity(createOrder.getQuantity());
    Customer customer = new Customer(createOrder.getCustomerId());
    Product product = new Product(createOrder.getProductId());

    order.creatOrder(product, customer, quantity);

    Order savedOrder = domainRepository.save(order);

    LOG.atInfo().log("Created new order %s", savedOrder.getId().getReference());

    return savedOrder;
  }
}
