package io.github.bhuwanupadhyay.rtms.order.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WebOrderApiTest.Initializer.class})
@Testcontainers
class WebOrderApiTest {

  @Container
  private static final PostgreSQLContainer SQL_CONTAINER =
      new PostgreSQLContainer()
          .withDatabaseName("order")
          .withUsername("postgres_user")
          .withPassword("postgres_password");

  @Container
  private static final RabbitMQContainer MQ_CONTAINER =
      new RabbitMQContainer().withUser("mq_user", "mq_password");

  @Test
  void ok_CreateOrder() {}

  @Test
  void badRequest_CreateOrder() {}

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
              // Data Source Configuration
              "spring.rabbitmq.host=" + MQ_CONTAINER.getHttpUrl(),
              "spring.rabbitmq.port=" + MQ_CONTAINER.getHttpPort(),
              "spring.rabbitmq.username=" + MQ_CONTAINER.getAdminUsername(),
              "spring.rabbitmq.password=" + MQ_CONTAINER.getAdminPassword(),
              // ----
              // Data Source Configuration
              "spring.datasource.url=" + SQL_CONTAINER.getJdbcUrl(),
              "spring.datasource.username=" + SQL_CONTAINER.getUsername(),
              "spring.datasource.password=" + SQL_CONTAINER.getPassword()
              // ----
              )
          .applyTo(configurableApplicationContext.getEnvironment());
    }
  }
}
