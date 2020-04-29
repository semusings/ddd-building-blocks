package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.v1.ProductsReserved;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Streams.class)
class AppStreamListener {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  final String PRODUCTS_RESERVED = "io.github.bhuwanupadhyay.rtms.v1.ProductsReserved";

  private final AppService appService;

  AppStreamListener(AppService appService) {
    this.appService = appService;
  }

  @StreamListener(target = Streams.IN)
  void on(ProductsReserved productsReserved) {
    LOG.atInfo().log("Received stream event %s:", PRODUCTS_RESERVED);
    this.appService.submitPayment(productsReserved.getOrderId().toString());
  }
}
