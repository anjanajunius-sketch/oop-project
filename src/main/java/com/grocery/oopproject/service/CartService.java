package com.grocery.oopproject.service;

import com.grocery.oopproject.domain.Cart;
import com.grocery.oopproject.domain.Customer;
import com.grocery.oopproject.domain.Product;
import com.grocery.oopproject.repository.CartRepository;
import com.grocery.oopproject.repository.CustomerRepository;
import com.grocery.oopproject.repository.ProductRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       CustomerRepository customerRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Cart getOrCreateCart(String customerId) {
        return cartRepository.findByCustomer_UserId(customerId).orElseGet(() -> {
            Customer c = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
            Cart cart = new Cart("CRT-" + UUID.randomUUID().toString().substring(0, 8), c);
            return cartRepository.save(cart);
        });
    }

    @Transactional
    public Cart addItem(String customerId, String productId, int qty) {
        Cart cart = getOrCreateCart(customerId);
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        cart.addItem(p, qty);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(String customerId, String productId) {
        Cart cart = getOrCreateCart(customerId);
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        cart.removeItem(p);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItemQuantity(String customerId, String productId, int qty) {
        Cart cart = getOrCreateCart(customerId);
        for (var item : cart.getItems()) {
            if (item.getProduct().getProductId().equals(productId)) {
                if (qty <= 0) {
                    cart.removeItem(item.getProduct());
                } else {
                    item.setQuantity(qty);
                }
                break;
            }
        }
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(String customerId) {
        Cart cart = getOrCreateCart(customerId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public Cart find(String customerId) {
        return getOrCreateCart(customerId);
    }
}
