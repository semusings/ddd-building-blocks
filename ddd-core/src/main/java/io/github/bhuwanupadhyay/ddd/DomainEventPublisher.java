package io.github.bhuwanupadhyay.ddd;

@FunctionalInterface
public interface DomainEventPublisher {

  void publish(DomainEvent domainEvent);
}
