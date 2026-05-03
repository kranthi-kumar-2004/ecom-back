package com.student.management.dto;

import java.time.LocalDateTime;

public class OrderSummary {

    private long id;
    private String order_id;
    private int user_id;
    private double total_price;
    private String delivery_status;
    private String payment_mode;
    private String payment_status;
    private LocalDateTime order_date;
    private long address_id;

    // ===== GETTERS =====

    public long getId() {
        return id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public LocalDateTime getOrder_date() {
        return order_date;
    }

    public long getAddress_id() {
        return address_id;
    }

    // ===== SETTERS =====

    public void setId(long id) {
        this.id = id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public void setOrder_date(LocalDateTime order_date) {
        this.order_date = order_date;
    }

    public void setAddress_id(long address_id) {
        this.address_id = address_id;
    }
}