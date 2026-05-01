package com.grocery.oopproject.repository;

import com.grocery.oopproject.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByCustomer_UserId(String customerId);
}
