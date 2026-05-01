package com.grocery.oopproject.service;

import com.grocery.oopproject.model.Cart;
import com.grocery.oopproject.model.Customer;
import com.grocery.oopproject.repository.CartRepository;
import com.grocery.oopproject.repository.CustomerRepository;
import com.grocery.oopproject.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository,
                           UserRepository userRepository,
                           CartRepository cartRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer find(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
    }

    @Transactional
    public Customer register(String name, String email, String rawPassword, String address, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        String userId = "U-C-" + UUID.randomUUID().toString().substring(0, 8);
        Customer c = new Customer(userId, name, email, passwordEncoder.encode(rawPassword), address, phone);
        c = customerRepository.save(c);

        Cart cart = new Cart("CRT-" + UUID.randomUUID().toString().substring(0, 8), c);
        cartRepository.save(cart);
        return c;
    }

    @Transactional
    public Customer update(String id, String name, String email, String address, String phone) {
        Customer c = find(id);
        c.setName(name);
        c.setEmail(email);
        c.setAddress(address);
        c.setPhone(phone);
        return customerRepository.save(c);
    }

    @Transactional
    public void delete(String id) {
        customerRepository.deleteById(id);
    }

    public Customer login(String email, String rawPassword) {
        Customer c = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No customer with email " + email));
        if (!passwordEncoder.matches(rawPassword, c.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return c;
    }
}
