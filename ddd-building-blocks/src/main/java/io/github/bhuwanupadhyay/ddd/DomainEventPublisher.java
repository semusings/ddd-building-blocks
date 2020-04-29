package io.github.bhuwanupadhyay.ddd;

// tag::code[]
@FunctionalInterface
public interface DomainEventPublisher {

  void publish(DomainEvent domainEvent);
}
// end::code[]
