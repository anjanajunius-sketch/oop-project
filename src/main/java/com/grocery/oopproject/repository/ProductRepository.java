package com.grocery.oopproject.repository;

import com.grocery.oopproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
