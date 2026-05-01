package com.grocery.oopproject.repository;

import com.grocery.oopproject.model.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByCustomer_UserId(String customerId);
}
