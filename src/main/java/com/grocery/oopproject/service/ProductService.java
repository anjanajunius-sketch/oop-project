package com.grocery.oopproject.service;

import com.grocery.oopproject.model.Product;
import com.grocery.oopproject.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product find(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    @Transactional
    public Product addProduct(String name, double price, int quantity) {
        String id = "P-" + UUID.randomUUID().toString().substring(0, 8);
        Product p = new Product(id, name, price, quantity);
        return productRepository.save(p);
    }

    @Transactional
    public Product updateProduct(String id, String name, double price, int quantity) {
        Product p = find(id);
        p.setName(name);
        p.setPrice(price);
        p.setQuantity(quantity);
        return productRepository.save(p);
    }

    @Transactional
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}
