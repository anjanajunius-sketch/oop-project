package com.grocery.oopproject.service;

import com.grocery.oopproject.model.Customer;
import com.grocery.oopproject.model.Product;
import com.grocery.oopproject.model.Review;
import com.grocery.oopproject.repository.CustomerRepository;
import com.grocery.oopproject.repository.ProductRepository;
import com.grocery.oopproject.repository.ReviewRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         ProductRepository productRepository,
                         CustomerRepository customerRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findByProduct(String productId) {
        return reviewRepository.findByProduct_ProductId(productId);
    }

    public Review find(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found: " + id));
    }

    @Transactional
    public Review addReview(String customerId, String productId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        String id = "R-" + UUID.randomUUID().toString().substring(0, 8);
        Review r = new Review(id, p, c, rating, comment, LocalDateTime.now().toString());
        return reviewRepository.save(r);
    }

    @Transactional
    public Review update(String id, int rating, String comment) {
        Review r = find(id);
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        r.setRating(rating);
        r.setComment(comment);
        return reviewRepository.save(r);
    }

    @Transactional
    public Review moderate(String id, boolean approve) {
        Review r = find(id);
        r.setModerated(true);
        if (!approve) {
            reviewRepository.delete(r);
            return r;
        }
        return reviewRepository.save(r);
    }

    @Transactional
    public void delete(String id) {
        reviewRepository.deleteById(id);
    }
}
