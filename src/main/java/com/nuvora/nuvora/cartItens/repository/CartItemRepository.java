package com.nuvora.nuvora.cartItens.repository;

import com.nuvora.nuvora.cartItens.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
