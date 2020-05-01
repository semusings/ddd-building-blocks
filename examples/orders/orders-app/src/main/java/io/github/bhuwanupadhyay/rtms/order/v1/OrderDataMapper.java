package io.github.bhuwanupadhyay.rtms.order.v1;

import io.github.bhuwanupadhyay.rtms.order.domain.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
abstract class OrderDataMapper {

  abstract Order toOrder(OrderDb orderDb);

  public abstract OrderDb toOrderData(Order entity);
}
