package com.nuvora.nuvora.orderItens.repository;

import com.nuvora.nuvora.orderItens.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
