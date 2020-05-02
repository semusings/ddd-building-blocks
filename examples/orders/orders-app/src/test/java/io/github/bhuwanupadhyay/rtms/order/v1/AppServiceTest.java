package io.github.bhuwanupadhyay.rtms.order.v1;

import static org.mockito.BDDMockito.given;

import io.github.bhuwanupadhyay.rtms.order.domain.OrderId;
import io.github.bhuwanupadhyay.rtms.order.v1.AppException.EntityNotFound;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {

  private AppService appService;

  @Mock private OrderDomainRepository domainRepository;

  @BeforeEach
  void setUp() {
    this.appService = new AppService(domainRepository);
  }

  @Test
  void givenOrderIsNotExist_whenConsume_thenShouldAbortFurtherProcessing() {
    final String alienOrderId = "alienOrderId";

    given(domainRepository.findOne(new OrderId(alienOrderId))).willReturn(Optional.empty());

    Assertions.assertThrows(
        EntityNotFound.class, () -> this.appService.submitPayment(alienOrderId));
  }
}
