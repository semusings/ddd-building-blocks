package io.github.bhuwanupadhyay.rtms.order.v1.persistance;

import org.springframework.data.repository.PagingAndSortingRepository;

import static io.github.bhuwanupadhyay.rtms.order.v1.persistance.SpringDataOrderRepository.OrderData;

public interface SpringDataOrderRepository extends PagingAndSortingRepository<OrderData, String> {

  class OrderData {}
}
