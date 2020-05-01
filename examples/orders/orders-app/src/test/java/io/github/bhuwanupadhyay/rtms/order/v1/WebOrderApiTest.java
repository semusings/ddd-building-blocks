package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.rtms.order.App;
import io.github.bhuwanupadhyay.rtms.orders.v1.http.ApiClient;
import io.github.bhuwanupadhyay.rtms.orders.v1.http.CreateOrder;
import io.github.bhuwanupadhyay.rtms.orders.v1.http.OrdersApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WebOrderApiTest.Initializer.class})
@Testcontainers
class WebOrderApiTest extends InfrastructureSetup {

  @LocalServerPort private Integer port;

  private OrdersApi ordersApi;

  @BeforeEach
  void setUp() {
    ApiClient apiClient = new ApiClient().setBasePath("http://localhost:" + port);
    this.ordersApi = apiClient.buildClient(OrdersApi.class);
  }

  @Test
  void ok_CreateOrder() {
    this.ordersApi.postOrders(
        new CreateOrder()
            .productId("P01")
            .customerId("C01")
            .contactPhone("+9779898989898")
            .deliveryAddress("Kathmandu, Nepal"));
  }

  @Test
  void badRequest_CreateOrder() {}
}
