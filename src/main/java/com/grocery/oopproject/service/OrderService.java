package com.grocery.oopproject.service;

import com.grocery.oopproject.domain.Cart;
import com.grocery.oopproject.domain.Customer;
import com.grocery.oopproject.domain.Order;
import com.grocery.oopproject.repository.CustomerRepository;
import com.grocery.oopproject.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        CartService cartService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.cartService = cartService;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findByCustomer(String customerId) {
        return orderRepository.findByCustomer_UserId(customerId);
    }

    public Order find(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    @Transactional
    public Order placeOrder(String customerId) {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        Cart cart = cartService.getOrCreateCart(customerId);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty for customer " + customerId);
        }
        double total = cart.calculateTotal();
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(orderId, cart.getCartId(), c, LocalDateTime.now().toString(), total);
        order.setSummary(order.generateOrderSummary());
        orderRepository.save(order);

        cartService.clearCart(customerId);
        return order;
    }

    @Transactional
    public Order updateDate(String orderId, String date) {
        Order o = find(orderId);
        o.setDate(date);
        o.setSummary(o.generateOrderSummary());
        return orderRepository.save(o);
    }

    @Transactional
    public void delete(String orderId) {
        orderRepository.deleteById(orderId);
    }
}
