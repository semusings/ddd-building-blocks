package io.github.bhuwanupadhyay.rtms.order.v1;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("rtms_order")
class OrderDb {
  private String orderId;
  private String productId;
  private String customerId;
  private Integer quantity;
  private String deliveryAddress;
  private String contactPhone;
}
