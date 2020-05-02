package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.rtms.v1.PaymentApproved;
import io.github.bhuwanupadhyay.rtms.v1.ProductShipped;
import io.github.bhuwanupadhyay.rtms.v1.ProductsReserved;
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
  class AppStreamListener {

    private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

    private final AppService appService;

    public AppStreamListener(AppService appService) {
      this.appService = appService;
    }

    @StreamListener(target = AppStream.IN)
    void on(ProductsReserved productsReserved) {
      LOG.atInfo().log("Received stream event %s:", ProductsReserved.class.getName());
      this.appService.submitPayment(productsReserved.getOrderId().toString());
    }

    @StreamListener(target = AppStream.IN)
    void on(PaymentApproved paymentApproved) {
      LOG.atInfo().log("Received stream event %s:", PaymentApproved.class.getName());
      this.appService.shipProducts(paymentApproved.getOrderId().toString());
    }

    @StreamListener(target = AppStream.IN)
    void on(ProductShipped productShipped) {
      LOG.atInfo().log("Received stream event %s:", ProductShipped.class.getName());
      this.appService.confirmOrder(productShipped.getOrderId().toString());
    }
  }
}
