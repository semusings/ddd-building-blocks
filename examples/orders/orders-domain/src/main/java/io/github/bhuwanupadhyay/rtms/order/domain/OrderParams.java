package io.github.bhuwanupadhyay.rtms.order.domain;

import java.util.Optional;

public interface OrderParams {

  Optional<String> getContactPhone();

  Optional<String> getDeliveryAddress();

  Optional<String> getCustomerId();

  Optional<String> getProductId();

  Optional<Integer> getQuantity();
}
