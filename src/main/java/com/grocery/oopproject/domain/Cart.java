package com.grocery.oopproject.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @Column(name = "cart_id", length = 64)
    private String cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public Cart(String cartId, Customer customer) {
        this.cartId = cartId;
        this.customer = customer;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(Product product, int qty) {
        for (CartItem existing : items) {
            if (existing.getProduct().getProductId().equals(product.getProductId())) {
                existing.setQuantity(existing.getQuantity() + qty);
                return;
            }
        }
        CartItem item = new CartItem(this, product, qty);
        items.add(item);
    }

    public void removeItem(Product product) {
        items.removeIf(ci -> ci.getProduct().getProductId().equals(product.getProductId()));
    }

    public double calculateTotal() {
        double total = 0.0;
        for (CartItem ci : items) {
            total += ci.getQuantity() * ci.getProduct().getPrice();
        }
        return total;
    }
}
