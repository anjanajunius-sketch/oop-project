package com.grocery.oopproject.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@DiscriminatorValue("CUSTOMER")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {

    @Column(length = 255)
    private String address;

    @Column(length = 40)
    private String phone;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    public Customer() {
    }

    public Customer(String userId, String name, String email, String password,
                    String address, String phone) {
        super(userId, name, email, password);
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    @Override
    public String getRoleLabel() {
        return "Customer";
    }
}
