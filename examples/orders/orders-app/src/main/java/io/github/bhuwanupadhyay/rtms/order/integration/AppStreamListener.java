package io.github.bhuwanupadhyay.rtms.order.integration;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.order.service.AppService;
import io.github.bhuwanupadhyay.rtms.v1.ProductsReserved;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Streams.class)
public class AppStreamListener {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  public final String PRODUCTS_RESERVED = "io.github.bhuwanupadhyay.rtms.v1.ProductsReserved";

  private final AppService appService;

  public AppStreamListener(AppService appService) {
    this.appService = appService;
  }

  @StreamListener(target = Streams.IN)
  public void on(ProductsReserved productsReserved) {
    LOG.atInfo().log("Received stream event %s:", PRODUCTS_RESERVED);
    this.appService.submitPayment(productsReserved.getOrderId().toString());
  }
}
