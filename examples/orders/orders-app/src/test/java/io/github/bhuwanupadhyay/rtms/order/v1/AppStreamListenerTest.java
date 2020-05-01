package io.github.bhuwanupadhyay.rtms.order.v1;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import io.github.bhuwanupadhyay.rtms.order.v1.AppStream.AppStreamListener;
import io.github.bhuwanupadhyay.rtms.v1.ProductsReserved;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppStreamListenerTest {

  private AppStreamListener listener;

  @Mock private AppService appService;

  @BeforeEach
  void setUp() {
    this.listener = new AppStreamListener(appService);
  }

  @Test
  void whenProductReserved_thenShouldSubmitPayment() {
    final String orderId = "orderId";

    this.listener.on(ProductsReserved.newBuilder().setOrderId(orderId).build());

    then(appService).should().submitPayment(eq(orderId));
  }
}
