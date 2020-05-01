package io.github.bhuwanupadhyay.rtms.order.v1;

import org.springframework.data.repository.PagingAndSortingRepository;

interface SpringDataOrderRepository extends PagingAndSortingRepository<OrderDb, String> {}
