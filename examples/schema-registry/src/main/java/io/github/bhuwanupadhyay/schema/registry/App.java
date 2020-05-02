package io.github.bhuwanupadhyay.schema.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.schema.registry.EnableSchemaRegistryServer;

@SpringBootApplication
@EnableSchemaRegistryServer
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
