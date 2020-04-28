package io.github.bhuwanupadhyay.rtms.order.stream;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.domain.Order;
import io.github.bhuwanupadhyay.rtms.order.domain.OrderId;
import io.github.bhuwanupadhyay.rtms.order.repository.AppDataException;
import io.github.bhuwanupadhyay.rtms.order.repository.OrderDomainRepository;
import io.github.bhuwanupadhyay.rtms.v1.ProductsReserved;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)
class AppStreamListener {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  public final String PRODUCTS_RESERVED = "io.github.bhuwanupadhyay.rtms.v1.ProductsReserved";

  private final OrderDomainRepository domainRepository;

  AppStreamListener(OrderDomainRepository domainRepository) {
    this.domainRepository = domainRepository;
  }

  @StreamListener
  public void on(ProductsReserved productsReserved) {
    LOG.atInfo().log("Received stream event %s:", PRODUCTS_RESERVED);

    Order order =
        domainRepository
            .findOne(new OrderId(productsReserved.getOrderId().toString()))
            .orElseThrow(AppDataException::new);

    order.createPayment();

    LOG.atInfo().log("Created payment request for an order %s.", order.getId().getReference());
  }
}
