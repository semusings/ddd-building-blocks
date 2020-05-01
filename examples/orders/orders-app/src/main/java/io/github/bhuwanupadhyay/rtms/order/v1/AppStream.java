package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.v1.ProductsReserved;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

interface AppStream {

  String IN = "rtmsOrdersIn";
  String OUT = "rtmsOrdersOut";

  @Input(IN)
  SubscribableChannel rtmsIn();

  @Output(OUT)
  MessageChannel rtmsOut();

  @Component
  @EnableBinding(AppStream.class)
  @RequiredArgsConstructor
  class AppStreamListener {

    private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

    final String PRODUCTS_RESERVED = "io.github.bhuwanupadhyay.rtms.v1.ProductsReserved";

    private final AppService appService;

    @StreamListener(target = AppStream.IN)
    void on(ProductsReserved productsReserved) {
      LOG.atInfo().log("Received stream event %s:", PRODUCTS_RESERVED);
      this.appService.submitPayment(productsReserved.getOrderId().toString());
    }
  }
}
