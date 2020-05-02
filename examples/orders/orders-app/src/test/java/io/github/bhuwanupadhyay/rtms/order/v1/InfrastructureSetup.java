package io.github.bhuwanupadhyay.rtms.order.v1;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;

import java.net.URI;

abstract class InfrastructureSetup {

  @Container
  private static final PostgreSQLContainer SQL_CONTAINER =
      new PostgreSQLContainer()
          .withDatabaseName("orders")
          .withUsername("postgres_user")
          .withPassword("postgres_password");

  @Container
  private static final RabbitMQContainer MQ_CONTAINER =
      new RabbitMQContainer().withUser("mq_user", "mq_password");

  //  @Container
  //  private static final GenericContainer EVENT_STORE =
  //      new GenericContainer(new RemoteDockerImage("eventstore/eventstore:latest"))
  //          .withExposedPorts(2113);

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
              // Data Source Configuration
              "spring.rabbitmq.host=" + URI.create(MQ_CONTAINER.getHttpUrl()).getHost(),
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
