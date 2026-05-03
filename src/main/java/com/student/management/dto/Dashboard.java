package com.student.management.dto;

import java.util.List;

public class Dashboard {

    private long users;
    private long products;
    private long totalOrders;
    private double totalRevenue;
    private List<OrderSummary> recentOrders;
  
    public long getUsers() {
        return users;
    }

    public long getProducts() {
        return products;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public List<OrderSummary> getRecentOrders() {
        return recentOrders;
    }
  
    public void setUsers(long users) {
        this.users = users;
    }

    public void setProducts(long products) {
        this.products = products;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void setRecentOrders(List<OrderSummary> recentOrders) {
        this.recentOrders = recentOrders;
    }
}