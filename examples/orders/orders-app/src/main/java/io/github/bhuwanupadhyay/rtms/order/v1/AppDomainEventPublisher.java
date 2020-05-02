package io.github.bhuwanupadhyay.rtms.order.v1;

import com.google.common.flogger.FluentLogger;
import io.github.bhuwanupadhyay.ddd.DomainEvent;
import io.github.bhuwanupadhyay.ddd.DomainEventPublisher;
import io.github.bhuwanupadhyay.rtms.order.domain.ConfirmationRequested;
import io.github.bhuwanupadhyay.rtms.order.domain.OrderPlaced;
import io.github.bhuwanupadhyay.rtms.order.domain.PaymentRequested;
import io.github.bhuwanupadhyay.rtms.order.domain.ShippingRequested;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.MessageStreamException;
import io.github.bhuwanupadhyay.rtms.v1.ReserveProducts;
import io.github.bhuwanupadhyay.rtms.v1.ShipProducts;
import io.github.bhuwanupadhyay.rtms.v1.SubmitPayment;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(AppStream.class)
class AppDomainEventPublisher implements DomainEventPublisher {

  private static final FluentLogger LOG = FluentLogger.forEnclosingClass();

  private final AppStream appStream;
  private final ApplicationEventPublisher eventPublisher;

  public AppDomainEventPublisher(AppStream appStream, ApplicationEventPublisher eventPublisher) {
    this.appStream = appStream;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void publish(DomainEvent domainEvent) {
    LOG.atInfo().log("Received domain event %s", domainEvent.getEventClassName());

    if (domainEvent.isInsideContext()) {
      eventPublisher.publishEvent(domainEvent);
      LOG.atInfo().log("Published domain event inside context %s", domainEvent.getEventClassName());
    }

    if (domainEvent.isOutsideContext()) {
      appStream.rtmsOut().send(StreamMessageFactory.create(domainEvent));
      LOG.atInfo().log(
          "Published domain event outside context %s", domainEvent.getEventClassName());
    }
  }

  static class StreamMessageFactory {

    static Message<?> create(DomainEvent domainEvent) {

      if (domainEvent instanceof OrderPlaced) {
        final OrderPlaced orderPlaced = (OrderPlaced) domainEvent;
        return MessageBuilder.withPayload(
                ReserveProducts.newBuilder()
                    .setOrderId(orderPlaced.getOrderId().getId())
                    .setProductId(orderPlaced.getProduct().getProductId())
                    .setQuantity(orderPlaced.getQuantity().getValue())
                    .build())
            .copyHeaders(buildHeaders(ReserveProducts.class))
            .build();
      } else if (domainEvent instanceof PaymentRequested) {
        final PaymentRequested paymentRequested = (PaymentRequested) domainEvent;
        return MessageBuilder.withPayload(
                SubmitPayment.newBuilder()
                    .setOrderId(paymentRequested.getOrderId().getId())
                    .setTotalAmount("USD 1000.00")
                    .build())
            .copyHeaders(buildHeaders(ReserveProducts.class))
            .build();
      } else if (domainEvent instanceof ShippingRequested) {
        final ShippingRequested paymentRequested = (ShippingRequested) domainEvent;
        return MessageBuilder.withPayload(
                ShipProducts.newBuilder().setOrderId(paymentRequested.getOrderId().getId()).build())
            .copyHeaders(buildHeaders(ReserveProducts.class))
            .build();
      } else if (domainEvent instanceof ConfirmationRequested) {
        LOG.atInfo().log("Notified to user for confirmation.");
      }

      throw new MessageStreamException(domainEvent);
    }

    private static MessageHeaders buildHeaders(Class<? extends SpecificRecord> clazz) {
      Map<String, Object> headers = new HashMap<>();
      headers.put("namespace", clazz.getName());
      return new MessageHeaders(headers);
    }
  }
}
