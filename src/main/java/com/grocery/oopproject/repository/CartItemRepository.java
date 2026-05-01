package com.grocery.oopproject.repository;

import com.grocery.oopproject.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
