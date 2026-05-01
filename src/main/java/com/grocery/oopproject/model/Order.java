package com.grocery.oopproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id", length = 64)
    private String orderId;

    @Column(name = "cart_id", length = 64)
    private String cartId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "order_date", nullable = false, length = 40)
    private String date;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(length = 2000)
    private String summary;

    public Order() {
    }

    public Order(String orderId, String cartId, Customer customer, String date, double totalAmount) {
        this.orderId = orderId;
        this.cartId = cartId;
        this.customer = customer;
        this.date = date;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String generateOrderSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ").append(orderId)
          .append(" placed on ").append(date)
          .append(" by ").append(customer == null ? "?" : customer.getName())
          .append(" — total LKR ").append(String.format("%.2f", totalAmount));
        return sb.toString();
    }
}
