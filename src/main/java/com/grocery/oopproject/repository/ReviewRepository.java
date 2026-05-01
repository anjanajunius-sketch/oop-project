package com.grocery.oopproject.repository;

import com.grocery.oopproject.model.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByProduct_ProductId(String productId);
}
