package io.github.bhuwanupadhyay.ddd;

// tag::code[]
import java.util.Objects;

public abstract class Entity<ID extends ValueObject> {

  public static final String ENTITY_ID_IS_REQUIRED = "EntityIdIsRequired";
  private final ID id;

  public Entity(ID id) {

    DomainAsserts.begin(id)
        .notNull(DomainError.create(this, ENTITY_ID_IS_REQUIRED))
        .end();

    this.id = id;
  }

  public ID getId() {
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
}
// end::code[]
