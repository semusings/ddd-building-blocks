package io.github.bhuwanupadhyay.ddd;

// tag::code[]
import java.util.Objects;

public abstract class Entity<Id extends ValueObject> {

  public static final String ENTITY_ID_IS_REQUIRED = "Entity Id is required.";

  private final Id id;

  public Entity(Id id) {

    DomainAsserts.raiseIfNull(
        id, DomainError.create(getObjectName() + ".id", ENTITY_ID_IS_REQUIRED));

    this.id = id;
  }

  public Id getId() {
    return this.id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    Entity<?> entity = (Entity<?>) o;
    return Objects.equals(this.id, entity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  public String getObjectName() {
    return this.getClass().getName();
  }
}
// end::code[]
