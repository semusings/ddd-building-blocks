package io.github.bhuwanupadhyay.ddd;

// tag::code[]

import java.util.UUID;

public abstract class DomainEvent {

  private final String eventId = UUID.randomUUID().toString();

  private final String eventClassName = getClass().getName();

  private final String domainEventType;

  protected DomainEvent(DomainEventType domainEventType) {
    DomainAsserts.raiseIfNull(
        domainEventType,
        DomainError.create(objectName() + ".domainEventType", "Domain event type is required."));
    this.domainEventType = domainEventType.name();
  }

  public String getEventId() {
    return eventId;
  }

  public String getEventClassName() {
    return eventClassName;
  }

  public String getDomainEventType() {
    return domainEventType;
  }

  public String objectName() {
    return getClass().getName();
  }

  public enum DomainEventType {
    /** Represents domain event is inside same bounded context only. */
    INSIDE_CONTEXT,
    /** Represents domain event is only for other bounded context. */
    OUTSIDE_CONTEXT,
    /**
     * Represents domain event is available for both i.e. inside same bounded context and other
     * bounded context.
     */
    BOTH
  }
}
// end::code[]
