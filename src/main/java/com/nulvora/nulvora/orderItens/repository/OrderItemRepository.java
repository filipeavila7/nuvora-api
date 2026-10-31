package com.nulvora.nulvora.orderItens.repository;

import com.nulvora.nulvora.orderItens.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
