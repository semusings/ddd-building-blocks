package io.github.bhuwanupadhyay.rtms.order.stream;

import io.github.bhuwanupadhyay.rtms.order.domain.OrderId;
import io.github.bhuwanupadhyay.rtms.order.repository.AppDataException;
import io.github.bhuwanupadhyay.rtms.order.repository.OrderDomainRepository;
import io.github.bhuwanupadhyay.rtms.v1.ProductsReserved;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AppStreamListenerTest {

  private AppStreamListener listener;

  @Mock private OrderDomainRepository domainRepository;

  @BeforeEach
  void setUp() {
    this.listener = new AppStreamListener(domainRepository);
  }

  @Test
  void givenOrderIsNotExist_whenConsume_thenShouldAbortFurtherProcessing() {
    final String alienOrderId = "alienOrderId";

    given(domainRepository.findOne(eq(new OrderId(alienOrderId)))).willReturn(Optional.empty());

    Assertions.assertThrows(
        AppDataException.class,
        () -> this.listener.on(ProductsReserved.newBuilder().setOrderId(alienOrderId).build()));
  }
}
