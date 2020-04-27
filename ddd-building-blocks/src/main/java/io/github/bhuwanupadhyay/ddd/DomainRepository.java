package io.github.bhuwanupadhyay.ddd;

// tag::code[]
import java.util.List;
import java.util.Optional;

public abstract class DomainRepository<T extends AggregateRoot<ID>, ID extends ValueObject> {

  public static final String ENTITY_IS_REQUIRED = "Entity is required.";

  private final DomainEventPublisher publisher;

  protected DomainRepository(DomainEventPublisher publisher) {
    this.publisher = publisher;
  }

  public abstract Optional<T> findOne(ID id);

  public T save(T entity) {
    DomainAsserts.raiseIfNull(
        entity, DomainError.create(objectName() + ".entity", ENTITY_IS_REQUIRED));
    T persisted = this.persist(entity);
    entity.getDomainEvents().forEach(publisher::publish);
    entity.clearDomainEvents();
    return persisted;
  }

  protected abstract T persist(T entity);

  public abstract List<T> findAll();

  public String objectName() {
    return getClass().getName();
  }
}
// end::code[]
