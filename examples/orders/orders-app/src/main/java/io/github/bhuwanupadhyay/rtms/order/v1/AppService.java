package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.domain.*;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.EntityNotFound;
import io.github.bhuwanupadhyay.rtms.orders.v1.CreateOrder;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
class AppService {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  private final OrderDomainRepository domainRepository;

  public AppService(OrderDomainRepository domainRepository) {
    this.domainRepository = domainRepository;
  }

  void submitPayment(String orderId) {

    LOG.atInfo().log("Preparing payment request for order an %s", orderId);

    Order order = getOrder(orderId);

    order.createPayment();

    LOG.atInfo().log("Created payment request for an order %s.", order.getId().getId());
  }

  void shipProducts(String orderId) {

    LOG.atInfo().log("Preparing shipping request for an order %s", orderId);

    Order order = getOrder(orderId);

    order.shipProducts();

    LOG.atInfo().log("Created shipping request for an order %s.", order.getId().getId());
  }

  void confirmOrder(String orderId) {

    LOG.atInfo().log("Preparing confirmation request for an order %s", orderId);

    Order order = getOrder(orderId);

    order.confirmOrder();

    LOG.atInfo().log("Created confirmation request for an order %s.", order.getId().getId());
  }

  OrderId placeOrder(CreateOrder createOrder) {
    OrderId orderId = new OrderId(UUID.randomUUID().toString());

    LOG.atInfo().log("Placing new order %s", orderId.getId());

    Order order = new Order(orderId);

    Quantity quantity = new Quantity(createOrder.getQuantity());
    Customer customer = new Customer(createOrder.getCustomerId());
    Product product = new Product(createOrder.getProductId());
    ContactPhone contactPhone = new ContactPhone(createOrder.getContactPhone());
    DeliveryAddress deliveryAddress = new DeliveryAddress(createOrder.getDeliveryAddress());

    order.placeOrder(product, customer, quantity, contactPhone, deliveryAddress);

    domainRepository.save(order);

    LOG.atInfo().log("Created new order %s", orderId.getId());

    return orderId;
  }

  private Order getOrder(String orderId) {
    return domainRepository
        .findOne(new OrderId(orderId))
        .orElseThrow(() -> new EntityNotFound("OrderNotFound"));
  }
}
