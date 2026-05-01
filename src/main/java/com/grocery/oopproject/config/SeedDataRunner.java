package com.grocery.oopproject.config;

import com.grocery.oopproject.model.Admin;
import com.grocery.oopproject.model.Cart;
import com.grocery.oopproject.model.Customer;
import com.grocery.oopproject.model.Review;
import com.grocery.oopproject.repository.AdminRepository;
import com.grocery.oopproject.repository.CartRepository;
import com.grocery.oopproject.repository.CustomerRepository;
import com.grocery.oopproject.repository.ProductRepository;
import com.grocery.oopproject.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds the demo accounts after schema/data init so passwords are real
 * BCrypt hashes (avoids checking brittle hash strings into version control).
 * Runs on every startup but is idempotent.
 */
@Component
public class SeedDataRunner implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    public SeedDataRunner(CustomerRepository customerRepository,
                          AdminRepository adminRepository,
                          CartRepository cartRepository,
                          ProductRepository productRepository,
                          ReviewRepository reviewRepository,
                          PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedAdmin("U-A1", "Site Admin", "admin@grocery.local", "admin123", "SUPER_ADMIN");
        Customer alice = seedCustomer("U-C1", "Alice Perera", "alice@example.com", "pass123",
                "12, Galle Road, Colombo 03", "0771234567", "CRT-1");
        seedCustomer("U-C2", "Bimal Silva", "bimal@example.com", "pass123",
                "45, Peradeniya Road, Kandy", "0719876543", "CRT-2");

        seedReview("R-1", "P-1", alice, 5,
                "Excellent rice, fluffy and aromatic.", "2026-04-30T10:00:00");
    }

    private void seedReview(String reviewId, String productId, Customer customer,
                            int rating, String comment, String createdAt) {
        if (reviewRepository.findById(reviewId).isPresent() || customer == null) {
            return;
        }
        productRepository.findById(productId).ifPresent(p -> {
            Review r = new Review(reviewId, p, customer, rating, comment, createdAt);
            reviewRepository.save(r);
        });
    }

    private void seedAdmin(String userId, String name, String email, String rawPassword, String roleName) {
        if (adminRepository.findById(userId).isPresent()) {
            return;
        }
        Admin a = new Admin(userId, name, email, passwordEncoder.encode(rawPassword), roleName);
        adminRepository.save(a);
    }

    private Customer seedCustomer(String userId, String name, String email, String rawPassword,
                                  String address, String phone, String cartId) {
        Customer existing = customerRepository.findById(userId).orElse(null);
        if (existing != null) {
            return existing;
        }
        Customer c = new Customer(userId, name, email, passwordEncoder.encode(rawPassword), address, phone);
        c = customerRepository.save(c);

        if (cartRepository.findById(cartId).isEmpty()) {
            cartRepository.save(new Cart(cartId, c));
        }
        return c;
    }
}
