package io.github.bhuwanupadhyay.rtms.order.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import io.github.bhuwanupadhyay.rtms.order.App;
import io.github.bhuwanupadhyay.rtms.orders.v1.http.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WebOrderApiTest.Initializer.class})
@Testcontainers
class WebOrderApiTest extends InfrastructureSetup {

  private static final ObjectMapper MAPPER = AppUtils.createObjectMapper();
  public static String CUSTOMER_ID;
  public static Integer QUANTITY;
  public static String CONTACT_PHONE;
  public static String DELIVERY_ADDRESS;
  public static String PRODUCT_ID;
  @LocalServerPort private Integer port;

  private OrdersApi ordersApi;
  @Autowired private JdbcTemplate jdbc;

  @BeforeEach
  void setUp() {

    CUSTOMER_ID = "C01";
    QUANTITY = 10;
    CONTACT_PHONE = "+9779898989898";
    DELIVERY_ADDRESS = "Kathmandu, Nepal";
    PRODUCT_ID = "P01";

    ApiClient apiClient = new ApiClient().setBasePath("http://localhost:" + port);
    this.ordersApi = apiClient.buildClient(OrdersApi.class);
  }

  @Test
  void ok_CreateOrder() {
    // given
    final CreateOrder createOrder =
        new CreateOrder()
            .productId(PRODUCT_ID)
            .customerId(CUSTOMER_ID)
            .quantity(QUANTITY)
            .contactPhone(CONTACT_PHONE)
            .deliveryAddress(DELIVERY_ADDRESS);
    // when
    final OrderResource result = this.ordersApi.postOrders(createOrder);
    // then
    assertNotNull(result);
    assertEquals(PRODUCT_ID, result.getProductId());
    assertEquals(CUSTOMER_ID, result.getCustomerId());
    assertEquals(QUANTITY, result.getQuantity());
    assertEquals(CONTACT_PHONE, result.getContactPhone());
    assertEquals(DELIVERY_ADDRESS, result.getDeliveryAddress());
  }

  @Test
  void badRequest_CreateOrder() {
    // given
    final CreateOrder createOrder =
        new CreateOrder()
            .productId(PRODUCT_ID)
            .customerId(CUSTOMER_ID)
            .quantity(0)
            .contactPhone(CONTACT_PHONE)
            .deliveryAddress(DELIVERY_ADDRESS);
    try {
      // when
      this.ordersApi.postOrders(createOrder);
      fail("Expected <BAD_REQUEST> but <OK>.");
    } catch (FeignException.BadRequest e) {
      assertTrue(
          "Should have QuantityValueShouldBePositive",
          e.getMessage().contains("QuantityValueShouldBePositive"));
      // then
    }
  }

  @Test
  void ok_GetOrders() throws JsonProcessingException {
    // given
    ok_CreateOrder();
    ok_CreateOrder();
    // when
    final OrderPageList actual = ordersApi.getOrders(null, null, 1, 20);
    // then
    assertEquals(2, actual.getContent().size());
    assertEquals(Long.valueOf(2), actual.getPage().getTotalElements());
  }

  @Test
  void ok_GetOrdersWithFilter() throws JsonProcessingException {
    // given
    ok_CreateOrder();
    PRODUCT_ID = "P02";
    ok_CreateOrder();
    final String filterJson = MAPPER.writeValueAsString(new OrderResource().productId(PRODUCT_ID));
    // when
    final OrderPageList actual = ordersApi.getOrders(filterJson, null, 1, 20);
    // then
    assertEquals(1, actual.getContent().size());
    assertEquals(Long.valueOf(1), actual.getPage().getTotalElements());
  }

  @Test
  void ok_GetOrdersWithNextPage() throws JsonProcessingException {
    // given
    ok_CreateOrder();
    PRODUCT_ID = "P02";
    ok_CreateOrder();
    PRODUCT_ID = "P03";
    ok_CreateOrder();
    PRODUCT_ID = "P04";
    ok_CreateOrder();
    PRODUCT_ID = "P05";
    ok_CreateOrder();
    PRODUCT_ID = "P06";
    ok_CreateOrder();
    final int expectedPageSize = 2;
    final Integer pageNumber = 1;
    final Long expectedTotalOrders = 6L;
    final Integer expectedTotalPages = Integer.valueOf(3);
    // when
    final OrderPageList actual = ordersApi.getOrders(null, null, pageNumber, expectedPageSize);
    // then
    assertEquals(expectedPageSize, actual.getContent().size());
    assertEquals(expectedTotalOrders, actual.getPage().getTotalElements());
    assertEquals(expectedTotalPages, actual.getPage().getTotalPages());
  }

  @Test
  void ok_GetOrderByOrderId() throws JsonProcessingException {
    // given
    ok_CreateOrder();
    final OrderPageList orders = ordersApi.getOrders(null, null, 1, 20);
    final String orderId = orders.getContent().get(0).getId();
    // when
    final OrderResource result = ordersApi.getOrdersByOrderId(orderId);
    // then
    assertNotNull(result);
    assertEquals(PRODUCT_ID, result.getProductId());
    assertEquals(CUSTOMER_ID, result.getCustomerId());
    assertEquals(QUANTITY, result.getQuantity());
    assertEquals(CONTACT_PHONE, result.getContactPhone());
    assertEquals(DELIVERY_ADDRESS, result.getDeliveryAddress());
  }

  @AfterEach
  void tearDown() {
    jdbc.update("delete from rtms_order");
  }
}
